package com.itextpdf.core.pdf;

import com.itextpdf.basics.PdfException;

public class PdfCatalog extends PdfObjectWrapper<PdfDictionary> {

    protected final PdfPagesTree pageTree;

    protected PdfCatalog(PdfDictionary pdfObject, PdfDocument pdfDocument) throws PdfException {
        super(pdfObject);
        if (pdfObject == null) {
            throw new PdfException(PdfException.DocumentHasNoCatalogObject);
        }
        this.pdfObject.makeIndirect(pdfDocument);
        this.pdfObject.put(PdfName.Type, PdfName.Catalog);
        pageTree = new PdfPagesTree(this);
    }

    protected PdfCatalog(PdfDocument pdfDocument) throws PdfException {
        this(new PdfDictionary(), pdfDocument);
    }

    public void addPage(PdfPage page) throws PdfException {
        if (page.isFlushed())
            throw new PdfException(PdfException.FlushedPageCannotBeAddedOrInserted, page);
        pageTree.addPage(page);
    }

    public void addPage(int index, PdfPage page) throws PdfException {
        if (page.isFlushed())
            throw new PdfException(PdfException.FlushedPageCannotBeAddedOrInserted, page);
        pageTree.addPage(index, page);
    }

    public PdfPage getPage(int pageNum) throws PdfException {
        return pageTree.getPage(pageNum);
    }

    public int getNumOfPages() {
        return pageTree.getNumOfPages();
    }

    public int getPageNum(PdfPage page) {
        return pageTree.getPageNum(page);
    }

    public boolean removePage(PdfPage page) throws PdfException {
        return pageTree.removePage(page);
    }

    public PdfPage removePage(int pageNum) throws PdfException {
        return pageTree.removePage(pageNum);
    }

    @Override
    public void flush() throws PdfException {
        if (getDocument().appendMode && !getPdfObject().getIndirectReference().checkState(PdfIndirectReference.Modified)) {
            PdfObject pages = pageTree.generateTree();
            if (pages.getIndirectReference().checkState(PdfIndirectReference.Modified)) {
                pdfObject.put(PdfName.Pages, pages);
                pdfObject.setModified();
                pdfObject.flush();
            }
        } else {
            pdfObject.put(PdfName.Pages, pageTree.generateTree());
            pdfObject.flush(false);
        }
    }

}
