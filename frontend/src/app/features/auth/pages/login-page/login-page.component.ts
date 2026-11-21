import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';
import { ToastService } from '../../../../core/services/toast.service';
import { ButtonComponent } from '../../../../shared/components/ui/button/button-component';
import { InputErrorComponent } from '../../../../shared/components/forms/input-error/input-error-component';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ButtonComponent, InputErrorComponent],
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.scss'],
})
export class LoginPageComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly toastService = inject(ToastService);

  readonly isLoading = signal(false);
  readonly is2faStep = signal(false);
  readonly preAuthToken = signal<string | null>(null);
  readonly errorMessage = signal<string | null>(null);

  loginForm!: FormGroup;
  totpForm!: FormGroup;

  ngOnInit(): void {
    this.initForms();
    this.checkOAuth2Callback();
  }

  private initForms(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });

    this.totpForm = this.fb.group({
      code: [
        '',
        [Validators.required, Validators.minLength(6), Validators.maxLength(6), Validators.pattern('^[0-9]+$')],
      ],
    });
  }

  private checkOAuth2Callback(): void {
    const params = this.route.snapshot.queryParams;

    if (params['error']) {
      const msg = params['message'] ? decodeURIComponent(params['message']) : 'Falha na autenticação com Google.';
      this.errorMessage.set(msg);
      this.toastService.showError(msg);
      return;
    }

    if (params['twoFactorRequired'] === 'true' && params['preAuthToken']) {
      this.preAuthToken.set(params['preAuthToken']);
      this.is2faStep.set(true);
      return;
    }

    if (params['token']) {
      this.isLoading.set(true);
      const token = params['token'];
      this.authService.accessToken.set(token);
      sessionStorage.setItem('coo_access_token', token);

      this.authService.getMe().subscribe({
        next: (user) => {
          this.authService.saveSession(token, user);
          this.toastService.showSuccess(`Bem-vindo(a), ${user.fullName}!`);
          this.router.navigate(['/home/dashboard']);
        },
        error: () => {
          this.isLoading.set(false);
          this.errorMessage.set('Não foi possível resgatar o perfil do usuário.');
        },
      });
    }
  }

  onSubmitLogin(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set(null);

    this.authService.login(this.loginForm.value).subscribe({
      next: (response) => {
        this.isLoading.set(false);
        if (response.is2faRequired && response.preAuthToken) {
          this.preAuthToken.set(response.preAuthToken);
          this.is2faStep.set(true);
        } else {
          this.toastService.showSuccess('Login realizado com sucesso!');
          const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/home/dashboard';
          this.router.navigateByUrl(returnUrl);
        }
      },
      error: (error) => {
        this.isLoading.set(false);
        const msg = error.error?.message || 'E-mail ou senha incorretos.';
        this.errorMessage.set(msg);
      },
    });
  }

  onSubmitTotp(): void {
    if (this.totpForm.invalid) {
      this.totpForm.markAllAsTouched();
      return;
    }

    const token = this.preAuthToken();
    if (!token) {
      this.backToLogin();
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set(null);

    this.authService
      .verifyTwoFactor({
        preAuthToken: token,
        code: this.totpForm.value.code,
      })
      .subscribe({
        next: () => {
          this.isLoading.set(false);
          this.toastService.showSuccess('Autenticação de dois fatores concluída com sucesso!');
          const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/home/dashboard';
          this.router.navigateByUrl(returnUrl);
        },
        error: (error) => {
          this.isLoading.set(false);
          const msg = error.error?.message || 'Código TOTP incorreto ou expirado.';
          this.errorMessage.set(msg);
        },
      });
  }

  loginWithGoogle(): void {
    window.location.href = this.authService.getGoogleLoginUrl();
  }

  backToLogin(): void {
    this.is2faStep.set(false);
    this.preAuthToken.set(null);
    this.errorMessage.set(null);
    this.totpForm.reset();
  }
}

