import { Component, Input } from '@angular/core';
import { LucideDownload, LucideFileText } from '@lucide/angular';
import { Attachment } from '../../../models/attachment.model';

@Component({
  selector: 'app-attachments-card',
  imports: [
    LucideDownload,
    LucideFileText,
  ],
  templateUrl: './attachments-info.html',
})
export class AttachmentsCardComponent {

  @Input() attachments: Attachment[] = [];
}
