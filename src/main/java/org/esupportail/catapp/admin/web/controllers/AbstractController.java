package org.esupportail.catapp.admin.web.controllers;

import fj.data.Option;
import fj.data.Stream;
import fj.data.Tree;
import org.esupportail.catapp.admin.domain.beans.DomaineDTO;
import org.esupportail.catapp.admin.domain.services.IDomaineService;
import org.esupportail.catapp.admin.domain.services.IService;
import org.esupportail.catapp.admin.web.utils.jsf.CustomTreeNode;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.ResourceBundle;

import static fj.data.Option.some;
import static fj.data.Stream.nil;
import static fj.data.Stream.single;
import static fj.data.Tree.node;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static org.esupportail.catapp.admin.domain.beans.DomaineDTO.domaineDTO;


public abstract class AbstractController<DTO, P> {

    protected final IService<DTO> service;

    protected P pojo;

    protected P originalPojo;

    protected TreeNode treeNode;

    protected final ResourceBundle resourceBundle;

    protected final FacesMessage serviceUnavailableMsg;

    protected final FacesMessage uniqueError;

    protected AbstractController(final IService<DTO> service) {
        this.service = service;
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getApplication().getMessageBundle();
        this.resourceBundle = ResourceBundle.getBundle("properties/i18n/Messages", facesContext.getViewRoot().getLocale());

        final String unavailableSummary = resourceBundle.getString("ERROR.SERVICE.UNAVAILABLE.TITLE");
        final String unavailabelDetail = resourceBundle.getString("ERROR.SERVICE.UNAVAILABLE.DESCRIPTION");
        this.serviceUnavailableMsg = new FacesMessage(SEVERITY_ERROR, unavailableSummary, unavailabelDetail);

        final String uniqueErrorMsg = resourceBundle.getString("ERROR.VALIDATION.UNIQUE");
        this.uniqueError = new FacesMessage(SEVERITY_ERROR, uniqueErrorMsg, uniqueErrorMsg);
    }

    abstract String create();

    abstract String update();

    abstract String delete();

    abstract  boolean isUnique();

    protected CustomTreeNode buildTreeNode(final IDomaineService domaineService) {
        try {
            final Tree<DomaineDTO> tree = domaineService.treeDomaines();
            final Tree<DomaineDTO> finalTree = node(domaineDTO("FAKEROOT", "", ""), single(tree));
            return new CustomTreeNode<>(finalTree, Option.<TreeNode>none());
        } catch (InterruptedException e) {
            final String msg = serviceUnavailableMsg.getDetail();
            final Tree<DomaineDTO> fakeroot = node(domaineDTO("", msg, msg),
                    single(node(domaineDTO("", msg, msg), Stream.<Tree<DomaineDTO>>nil())));
            final CustomTreeNode<DomaineDTO> node = new CustomTreeNode<>(fakeroot, Option.<TreeNode>none());
            node.getChildren().iterator().next().setSelectable(false);
            return node;
        }
    }

    public P getPojo() {
        return pojo;
    }

    public void setPojo(final P pojo) {
        if (pojo != null) {
            this.pojo = pojo;
        }
    }

    public P getOriginalPojo() {
        return originalPojo;
    }

    public void setOriginalPojo(final P originalPojo) {
        if (originalPojo != null) {
            this.originalPojo = originalPojo;
        }
    }

    public TreeNode getTreeNode() {
        return treeNode;
    }
}
