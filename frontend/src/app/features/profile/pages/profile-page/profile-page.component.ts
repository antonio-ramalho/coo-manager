import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import {
  LucideArrowLeft,
  LucideKeyRound,
  LucideSmartphone,
  LucideUser,
} from '@lucide/angular';
import { AuthService } from '../../../../core/services/auth.service';
import { ProfileService } from '../../../../core/services/profile.service';
import { ToastService } from '../../../../core/services/toast.service';
import { UserAuth } from '../../../../core/models/auth.models';
import { ChangePasswordRequest } from '../../../../core/models/change-password.model';
import { ProfileInfoCardComponent } from '../../components/profile-info-card/profile-info-card.component';
import { ChangePasswordCardComponent } from '../../components/change-password-card/change-password-card.component';
import { TwoFactorCardComponent } from '../../components/two-factor-card/two-factor-card.component';
import { TwoFactorSetupModalComponent } from '../../components/two-factor-setup-modal/two-factor-setup-modal.component';
import { TwoFactorDisableModalComponent } from '../../components/two-factor-disable-modal/two-factor-disable-modal.component';
import { LoadingSpinnerComponent } from '../../../../shared/components/ui/loading-spinner/loading-spinner.component';

export type ProfileTab = 'INFO' | 'PASSWORD' | 'TWO_FACTOR';

@Component({
  selector: 'app-profile-page',
  standalone: true,
  imports: [
    CommonModule,
    LucideArrowLeft,
    LucideUser,
    LucideKeyRound,
    LucideSmartphone,
    ProfileInfoCardComponent,
    ChangePasswordCardComponent,
    TwoFactorCardComponent,
    TwoFactorSetupModalComponent,
    TwoFactorDisableModalComponent,
    LoadingSpinnerComponent,
  ],
  templateUrl: './profile-page.component.html',
  styleUrl: './profile-page.component.scss',
})
export class ProfilePageComponent implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly profileService = inject(ProfileService);
  private readonly toastService = inject(ToastService);
  private readonly router = inject(Router);

  readonly activeTab = signal<ProfileTab>('INFO');
  readonly user = signal<UserAuth | null>(null);
  readonly isLoading = signal(true);
  readonly isChangingPassword = signal(false);
  readonly is2faSubmitting = signal(false);

  // Estados dos modais de 2FA
  isSetupModalOpen = false;
  isDisableModalOpen = false;
  setupSecret = '';
  setupQrUri = '';

  ngOnInit(): void {
    this.loadUserData();
  }

  loadUserData(): void {
    this.isLoading.set(true);
    this.authService.getMe().subscribe({
      next: (userData) => {
        this.user.set(userData);
        this.isLoading.set(false);
      },
      error: () => {
        this.toastService.showError('Não foi possível carregar os dados do perfil.');
        this.isLoading.set(false);
      },
    });
  }

  setTab(tab: ProfileTab): void {
    this.activeTab.set(tab);
  }

  goBack(): void {
    this.router.navigate(['/home/dashboard']);
  }

  onPasswordChange(payload: ChangePasswordRequest): void {
    const currentUser = this.user();
    if (!currentUser) return;

    this.isChangingPassword.set(true);
    this.profileService.changePassword(currentUser.id, payload).subscribe({
      next: () => {
        this.toastService.showSuccess('Senha alterada com sucesso!');
        this.isChangingPassword.set(false);
      },
      error: (err) => {
        const msg = err.error?.message || err.error?.error || 'Erro ao alterar a senha.';
        this.toastService.showError(msg);
        this.isChangingPassword.set(false);
      },
    });
  }

  onStartSetup(): void {
    this.is2faSubmitting.set(true);
    this.authService.setupTwoFactor().subscribe({
      next: (res) => {
        this.setupSecret = res.secret;
        this.setupQrUri = res.qrCodeUri;
        this.isSetupModalOpen = true;
        this.is2faSubmitting.set(false);
      },
      error: () => {
        this.toastService.showError('Erro ao iniciar configuração do 2FA.');
        this.is2faSubmitting.set(false);
      },
    });
  }

  onConfirm2fa(code: string): void {
    this.is2faSubmitting.set(true);
    this.authService
      .confirmTwoFactor({
        secret: this.setupSecret,
        code,
      })
      .subscribe({
        next: () => {
          this.toastService.showSuccess('Autenticação em duas etapas ativada com sucesso!');
          this.isSetupModalOpen = false;
          this.is2faSubmitting.set(false);
          this.loadUserData();
        },
        error: (err) => {
          const msg = err.error?.message || err.error?.error || 'Código de verificação incorreto ou expirado.';
          this.toastService.showError(msg);
          this.is2faSubmitting.set(false);
        },
      });
  }

  onStartDisable(): void {
    this.isDisableModalOpen = true;
  }

  onDisable2fa(code: string): void {
    this.is2faSubmitting.set(true);
    this.authService.disableTwoFactor(code).subscribe({
      next: () => {
        this.toastService.showSuccess('Autenticação em duas etapas desativada.');
        this.isDisableModalOpen = false;
        this.is2faSubmitting.set(false);
        this.loadUserData();
      },
      error: (err) => {
        const msg = err.error?.message || err.error?.error || 'Código incorreto para desativação.';
        this.toastService.showError(msg);
        this.is2faSubmitting.set(false);
      },
    });
  }
}
