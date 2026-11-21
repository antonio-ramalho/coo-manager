import { ComponentFixture, TestBed } from '@angular/core/testing';
import { describe, expect, it, beforeEach, vi } from 'vitest';
import { BaseModalComponent } from './base-modal.component';

describe('BaseModalComponent', () => {
  let component: BaseModalComponent;
  let fixture: ComponentFixture<BaseModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [BaseModalComponent],
    });

    fixture = TestBed.createComponent(BaseModalComponent);
    component = fixture.componentInstance;
  });

  it('deve inicializar fechado por padrão', () => {
    expect(component.isOpen).toBe(false);
  });

  it('deve emitir evento de close ao chamar onClose()', () => {
    const closeSpy = vi.fn();
    component.close.subscribe(closeSpy);

    component.onClose();
    expect(closeSpy).toHaveBeenCalled();
  });

  it('deve fechar ao clicar no backdrop se closeOnBackdrop for true', () => {
    const closeSpy = vi.fn();
    component.close.subscribe(closeSpy);
    component.closeOnBackdrop = true;

    component.onBackdropClick();
    expect(closeSpy).toHaveBeenCalled();
  });

  it('não deve fechar ao clicar no backdrop se closeOnBackdrop for false', () => {
    const closeSpy = vi.fn();
    component.close.subscribe(closeSpy);
    component.closeOnBackdrop = false;

    component.onBackdropClick();
    expect(closeSpy).not.toHaveBeenCalled();
  });

  it('deve fechar ao pressionar Escape quando estiver aberto', () => {
    const closeSpy = vi.fn();
    component.close.subscribe(closeSpy);
    component.isOpen = true;
    component.closeOnEscape = true;

    const mockEvent = new KeyboardEvent('keydown', { key: 'Escape' });
    component.onEscapeKey(mockEvent);

    expect(closeSpy).toHaveBeenCalled();
  });

  it('deve aplicar a classe de tamanho correta de acordo com @Input() size', () => {
    component.size = 'sm';
    expect(component.getSizeClass()).toBe('modal-sm');

    component.size = 'lg';
    expect(component.getSizeClass()).toBe('modal-lg');

    component.size = 'xl';
    expect(component.getSizeClass()).toBe('modal-xl');
  });

  it('deve aplicar estilo de min-height quando @Input() minHeight for informado', () => {
    component.isOpen = true;
    component.minHeight = '580px';
    fixture.detectChanges();

    const cardEl = fixture.nativeElement.querySelector('.modal-card') as HTMLElement;
    expect(cardEl?.style.minHeight).toBe('580px');
  });

  it('deve aplicar classe no-padding quando @Input() noPadding for true', () => {
    component.isOpen = true;
    component.noPadding = true;
    fixture.detectChanges();

    const bodyEl = fixture.nativeElement.querySelector('.modal-body');
    expect(bodyEl?.classList.contains('no-padding')).toBe(true);
  });
});
