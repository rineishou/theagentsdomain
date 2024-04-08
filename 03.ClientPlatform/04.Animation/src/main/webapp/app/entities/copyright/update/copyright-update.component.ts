import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { CopyrightService } from '../service/copyright.service';
import { ICopyright } from '../copyright.model';
import { CopyrightFormService, CopyrightFormGroup } from './copyright-form.service';

@Component({
  standalone: true,
  selector: 'jhi-copyright-update',
  templateUrl: './copyright-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CopyrightUpdateComponent implements OnInit {
  isSaving = false;
  copyright: ICopyright | null = null;

  editForm: CopyrightFormGroup = this.copyrightFormService.createCopyrightFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected copyrightService: CopyrightService,
    protected copyrightFormService: CopyrightFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ copyright }) => {
      this.copyright = copyright;
      if (copyright) {
        this.updateForm(copyright);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('highwayacApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const copyright = this.copyrightFormService.getCopyright(this.editForm);
    if (copyright.id !== null) {
      this.subscribeToSaveResponse(this.copyrightService.update(copyright));
    } else {
      this.subscribeToSaveResponse(this.copyrightService.create(copyright));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICopyright>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(copyright: ICopyright): void {
    this.copyright = copyright;
    this.copyrightFormService.resetForm(this.editForm, copyright);
  }
}
