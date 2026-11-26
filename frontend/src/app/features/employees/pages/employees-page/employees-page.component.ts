import { Component, DestroyRef, OnInit, TemplateRef, ViewChild, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BreakpointObserver } from '@angular/cdk/layout';
import { toSignal } from '@angular/core/rxjs-interop';
import { map } from 'rxjs';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ConnectedPosition, OverlayModule } from '@angular/cdk/overlay';
import { LucideEllipsis } from '@lucide/angular';
import { DesktopOnlyNoticeComponent } from '../../../../shared/components/feedback/desktop-only-notice/desktop-only-notice.component';
import { AuthService } from '../../../../core/services/auth.service';
import { EmployeeService } from '../../../../core/services/employee.service';
import { SubNavService } from '../../../../core/services/sub-nav.service';
import { SubNavTab } from '../../../../shared/components/layout/sub-nav/sub-nav.component';
import { ToastService } from '../../../../core/services/toast.service';
import { EmployeeMin, RoleRequest } from '../../../../core/models/employee.models';
import { RoleAuth } from '../../../../core/models/auth.models';
import { GenericTableComponent } from '../../../../shared/components/tables/generic-table/generic-table.component';
import { TableColumn } from '../../../../shared/components/tables/generic-table/table-column.model';
import { PaginationComponent } from '../../../../shared/components/lists/generic-pagination/pagination-component';
import { SearchBarComponent } from '../../../../shared/components/lists/search-bar/search-bar-component';
import { ButtonComponent } from '../../../../shared/components/ui/button/button-component';
import { InputErrorComponent } from '../../../../shared/components/forms/input-error/input-error-component';

import { ActionMenuComponent } from '../../../../shared/components/ui/action-menu/action-menu.component';
import { ActionMenuItem } from '../../../../shared/components/ui/action-menu/action-menu-item.model';
import { BaseModalComponent } from '../../../../shared/components/feedback/base-modal/base-modal.component';

export interface PermissionGroup {
  module: string;
  name: string;
  permissions: { key: string; label: string }[];
}

@Component({
  selector: 'app-employees-page',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    GenericTableComponent,
    PaginationComponent,
    SearchBarComponent,
    ButtonComponent,
    InputErrorComponent,
    ActionMenuComponent,
    BaseModalComponent,
    DesktopOnlyNoticeComponent,
  ],
  templateUrl: './employees-page.component.html',
  styleUrls: ['./employees-page.component.scss'],
})
export class EmployeesPageComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly employeeService = inject(EmployeeService);
  private readonly subNavService = inject(SubNavService);
  private readonly toastService = inject(ToastService);
  private readonly destroyRef = inject(DestroyRef);
  private readonly breakpointObserver = inject(BreakpointObserver);

  readonly isDesktop = toSignal(
    this.breakpointObserver.observe('(min-width: 768px)').pipe(map((result) => result.matches)),
    { initialValue: true },
  );

  readonly canManageUsers = computed(() => this.authService.hasPermission('USER_MANAGE'));

  isCurrentLoggedInUser(userId: number | null | undefined): boolean {
    if (!userId) return false;
    return this.authService.currentUser()?.id === userId;
  }

  readonly tabs: SubNavTab[] = [
    { label: 'Equipe', route: '/home/funcionarios', exact: true },
  ];

  @ViewChild('actionsTpl', { static: true }) actionsTpl!: TemplateRef<any>;
  @ViewChild('rolesTpl', { static: true }) rolesTpl!: TemplateRef<any>;
  @ViewChild('statusTpl', { static: true }) statusTpl!: TemplateRef<any>;

  readonly employees = signal<EmployeeMin[]>([]);
  readonly roles = signal<RoleAuth[]>([]);
  readonly isLoading = signal<boolean>(true);
  readonly totalItems = signal<number>(0);
  readonly currentPage = signal<number>(1);
  readonly pageSize = 10;
  readonly searchTerm = signal<string>('');

  getEmployeeActions(emp: EmployeeMin): ActionMenuItem[] {
    const actions: ActionMenuItem[] = [
      {
        label: 'Editar',
        action: () => this.openEditEmployeeModal(emp),
      },
    ];

    if (this.canManageUsers() && !this.isCurrentLoggedInUser(emp.id)) {
      if (emp.status === 'ACTIVE') {
        actions.push({
          label: 'Desativar',
          danger: true,
          dividerAbove: true,
          action: () => this.toggleEmployeeStatus(emp),
        });
      } else {
        actions.push({
          label: 'Ativar',
          success: true,
          dividerAbove: true,
          action: () => this.toggleEmployeeStatus(emp),
        });
      }
    }

    return actions;
  }

  constructor() {
    this.subNavService.setTabs(this.tabs);
    this.destroyRef.onDestroy(() => {
      this.subNavService.clearTabs();
    });
  }

  readonly isEmployeeModalOpen = signal<boolean>(false);
  readonly isRolesModalOpen = signal<boolean>(false);
  readonly editingEmployeeId = signal<number | null>(null);
  readonly isSaving = signal<boolean>(false);
  readonly isSavingRole = signal<boolean>(false);

  readonly editingRole = signal<RoleAuth | null>(null);

  employeeForm!: FormGroup;
  roleForm!: FormGroup;

  columns: TableColumn<EmployeeMin>[] = [];

  readonly permissionGroups: PermissionGroup[] = [
    {
      module: 'clients',
      name: 'Clientes',
      permissions: [
        { key: 'CLIENT_READ', label: 'Visualizar Clientes' },
        { key: 'CLIENT_WRITE', label: 'Cadastrar / Editar Clientes' },
      ],
    },
    {
      module: 'farmers',
      name: 'Produtores Rurais',
      permissions: [
        { key: 'FARMER_READ', label: 'Visualizar Produtores' },
        { key: 'FARMER_WRITE', label: 'Cadastrar / Editar Produtores' },
      ],
    },
    {
      module: 'contracts',
      name: 'Editais e Contratos',
      permissions: [
        { key: 'CONTRACT_READ', label: 'Visualizar Editais' },
        { key: 'CONTRACT_WRITE', label: 'Gerenciar Editais e Quotas' },
      ],
    },
    {
      module: 'inputs',
      name: 'Insumos e Estoque',
      permissions: [
        { key: 'INPUT_READ', label: 'Visualizar Insumos' },
        { key: 'INPUT_WRITE', label: 'Gerenciar Lotes e Compras' },
      ],
    },
    {
      module: 'sales',
      name: 'Vendas e Logística',
      permissions: [
        { key: 'SALES_READ', label: 'Visualizar Vendas' },
        { key: 'SALES_WRITE', label: 'Emitir Vendas e Termos' },
      ],
    },
    {
      module: 'users',
      name: 'Administração',
      permissions: [
        { key: 'USER_MANAGE', label: 'Administrar Funcionários e Cargos' },
      ],
    },
  ];

  ngOnInit(): void {
    this.initForms();
    this.initColumns();
    this.loadRoles();
    this.loadEmployees();
  }

  private initColumns(): void {
    this.columns = [
      { key: 'fullName', header: 'Nome do Funcionário', field: 'fullName' },
      { key: 'email', header: 'E-mail Institucional', field: 'email' },
      { key: 'roles', header: 'Cargos Atribuídos', cellTemplate: this.rolesTpl },
      { key: 'status', header: 'Status', cellTemplate: this.statusTpl },
      { key: 'actions', header: 'Ações', cellTemplate: this.actionsTpl, align: 'right' },
    ];
  }

  private initForms(): void {
    this.employeeForm = this.fb.group({
      fullName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: [''],
      roleIds: [[], Validators.required],
    });

    this.roleForm = this.fb.group({
      name: ['', Validators.required],
      description: [''],
      permissions: [[], Validators.required],
    });
  }

  loadRoles(): void {
    const coopId = this.authService.tenantId();
    if (!coopId) return;

    this.employeeService.getRolesForCooperative(coopId).subscribe({
      next: (roles) => this.roles.set(roles),
    });
  }

  loadEmployees(): void {
    const coopId = this.authService.tenantId();
    if (!coopId) {
      this.isLoading.set(false);
      return;
    }

    this.isLoading.set(true);
    const pageIndex = this.currentPage() - 1;

    this.employeeService
      .findAllByCooperative(coopId, pageIndex, this.pageSize, this.searchTerm())
      .subscribe({
        next: (page) => {
          this.employees.set(page.content);
          this.totalItems.set(page.totalElements);
          this.isLoading.set(false);
        },
        error: () => {
          this.isLoading.set(false);
          this.toastService.showError('Não foi possível carregar a lista de funcionários.');
        },
      });
  }

  onSearch(term: string): void {
    this.searchTerm.set(term);
    this.currentPage.set(1);
    this.loadEmployees();
  }

  onPageChange(page: number): void {
    this.currentPage.set(page);
    this.loadEmployees();
  }

  openNewEmployeeModal(): void {
    this.editingEmployeeId.set(null);
    this.employeeForm.reset({ roleIds: [] });
    this.employeeForm.get('password')?.setValidators([Validators.required, Validators.minLength(6)]);
    this.employeeForm.get('password')?.updateValueAndValidity();
    this.isEmployeeModalOpen.set(true);
  }

  openEditEmployeeModal(emp: EmployeeMin): void {
    this.editingEmployeeId.set(emp.id);
    this.employeeForm.get('password')?.clearValidators();
    this.employeeForm.get('password')?.updateValueAndValidity();

    this.employeeService.findById(emp.id).subscribe({
      next: (full) => {
        const roleIds = full.roles.map((r) => r.id);
        this.employeeForm.patchValue({
          fullName: full.fullName,
          email: full.email,
          password: '',
          roleIds,
        });
        this.isEmployeeModalOpen.set(true);
      },
    });
  }

  closeEmployeeModal(): void {
    this.isEmployeeModalOpen.set(false);
  }

  toggleRoleSelection(roleId: number): void {
    if (this.isCurrentLoggedInUser(this.editingEmployeeId())) {
      return;
    }
    const current: number[] = this.employeeForm.get('roleIds')?.value || [];
    const index = current.indexOf(roleId);
    if (index > -1) {
      current.splice(index, 1);
    } else {
      current.push(roleId);
    }
    this.employeeForm.patchValue({ roleIds: [...current] });
  }

  isRoleSelected(roleId: number): boolean {
    const current: number[] = this.employeeForm.get('roleIds')?.value || [];
    return current.includes(roleId);
  }

  onSaveEmployee(): void {
    if (this.employeeForm.invalid) {
      this.employeeForm.markAllAsTouched();
      return;
    }

    const coopId = this.authService.tenantId();
    if (!coopId) return;

    this.isSaving.set(true);
    const formVal = this.employeeForm.value;
    const editingId = this.editingEmployeeId();

    if (editingId) {
      const isSelf = this.isCurrentLoggedInUser(editingId);
      this.employeeService.update(editingId, {
        fullName: formVal.fullName,
        email: formVal.email,
        roleIds: isSelf ? undefined : formVal.roleIds,
      }).subscribe({
        next: () => {
          this.isSaving.set(false);
          this.closeEmployeeModal();
          this.toastService.showSuccess('Funcionário atualizado com sucesso!');
          this.loadEmployees();
        },
        error: (err) => {
          this.isSaving.set(false);
          this.toastService.showError(err.error?.message || 'Erro ao atualizar funcionário.');
        },
      });
    } else {
      this.employeeService.insert({
        fullName: formVal.fullName,
        email: formVal.email,
        password: formVal.password,
        cooperativeId: coopId,
        roleIds: formVal.roleIds,
      }).subscribe({
        next: () => {
          this.isSaving.set(false);
          this.closeEmployeeModal();
          this.toastService.showSuccess('Funcionário cadastrado com sucesso!');
          this.loadEmployees();
        },
        error: (err) => {
          this.isSaving.set(false);
          this.toastService.showError(err.error?.message || 'Erro ao cadastrar funcionário.');
        },
      });
    }
  }

  toggleEmployeeStatus(emp: EmployeeMin): void {
    if (this.isCurrentLoggedInUser(emp.id)) {
      this.toastService.showError('Não é permitido inativar a própria conta.');
      return;
    }

    const action = emp.status === 'ACTIVE'
      ? this.employeeService.deactivate(emp.id)
      : this.employeeService.activate(emp.id);

    action.subscribe({
      next: () => {
        this.toastService.showSuccess(`Status do funcionário alterado com sucesso!`);
        this.loadEmployees();
      },
      error: (err) => {
        this.toastService.showError(err.error?.message || 'Erro ao alterar status.');
      },
    });
  }

  openRolesModal(): void {
    this.isRolesModalOpen.set(true);
    this.resetRoleForm();
  }

  closeRolesModal(): void {
    this.isRolesModalOpen.set(false);
  }

  resetRoleForm(): void {
    this.editingRole.set(null);
    this.roleForm.reset({ permissions: [] });
  }

  editRole(role: RoleAuth): void {
    this.editingRole.set(role);
    this.roleForm.patchValue({
      name: role.name,
      description: role.description,
      permissions: role.permissions,
    });
  }

  togglePermission(key: string): void {
    const current: string[] = this.roleForm.get('permissions')?.value || [];
    const idx = current.indexOf(key);
    if (idx > -1) {
      current.splice(idx, 1);
    } else {
      current.push(key);
    }
    this.roleForm.patchValue({ permissions: [...current] });
  }

  isPermissionChecked(key: string): boolean {
    const current: string[] = this.roleForm.get('permissions')?.value || [];
    return current.includes(key);
  }

  onSaveRole(): void {
    if (this.roleForm.invalid) {
      this.roleForm.markAllAsTouched();
      return;
    }

    if (this.isSavingRole()) {
      return;
    }

    const coopId = this.authService.tenantId();
    const editing = this.editingRole();
    const formVal = this.roleForm.value;

    const dto: RoleRequest = {
      name: formVal.name,
      description: formVal.description,
      cooperativeId: coopId,
      permissions: formVal.permissions,
    };

    this.isSavingRole.set(true);

    const request$ = editing
      ? this.employeeService.updateRole(editing.id, dto)
      : this.employeeService.saveRole(dto);

    request$.subscribe({
      next: () => {
        this.isSavingRole.set(false);
        this.toastService.showSuccess('Cargo salvo com sucesso!');
        this.resetRoleForm();
        this.loadRoles();
      },
      error: (err) => {
        this.isSavingRole.set(false);
        this.toastService.showError(err.error?.message || 'Falha ao salvar cargo.');
      },
    });
  }

  deleteRole(role: RoleAuth): void {
    if (role.systemRole) {
      this.toastService.showWarning('Cargos padrão do sistema não podem ser excluídos.');
      return;
    }

    if (confirm(`Tem certeza que deseja excluir o cargo "${role.name}"?`)) {
      this.employeeService.deleteRole(role.id).subscribe({
        next: () => {
          this.toastService.showSuccess('Cargo excluído com sucesso!');
          this.loadRoles();
        },
      });
    }
  }
}