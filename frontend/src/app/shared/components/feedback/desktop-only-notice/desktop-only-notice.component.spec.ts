import { describe, expect, it } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DesktopOnlyNoticeComponent } from './desktop-only-notice.component';

describe('DesktopOnlyNoticeComponent', () => {
  it('should render featureName properly', () => {
    TestBed.configureTestingModule({
      imports: [DesktopOnlyNoticeComponent],
    });

    const fixture: ComponentFixture<DesktopOnlyNoticeComponent> = TestBed.createComponent(
      DesktopOnlyNoticeComponent,
    );
    fixture.componentRef.setInput('featureName', 'Gestão de Clientes');
    fixture.detectChanges();

    const element = fixture.nativeElement as HTMLElement;
    expect(element.textContent).toContain('Gestão de Clientes');
    expect(element.textContent).toContain('Tela maior necessária');
  });
});
