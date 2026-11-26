import { Component, Input, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LucideBuilding, LucideMail, LucideShield } from '@lucide/angular';
import { UserAuth } from '../../../../core/models/auth.models';

@Component({
  selector: 'app-profile-info-card',
  standalone: true,
  imports: [CommonModule, LucideMail, LucideBuilding, LucideShield],
  templateUrl: './profile-info-card.component.html',
  styleUrl: './profile-info-card.component.scss',
})
export class ProfileInfoCardComponent {
  @Input({ required: true }) user!: UserAuth;

  readonly userInitials = computed(() => {
    if (!this.user?.fullName) return 'U';
    const parts = this.user.fullName.trim().split(/\s+/);
    if (parts.length === 1) return parts[0].substring(0, 2).toUpperCase();
    return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
  });
}
