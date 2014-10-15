package org.esupportail.catapp.admin.web.controllers;

import fj.Effect;
import fj.F;
import fj.data.Option;
import fj.data.Tree;
import org.esupportail.catapp.admin.domain.beans.DomaineDTO;
import org.esupportail.catapp.admin.domain.exceptions.CrudException;
import org.esupportail.catapp.admin.domain.services.IDomaineService;
import org.esupportail.catapp.admin.web.pojo.ApplicationPojo;
import org.esupportail.catapp.admin.web.pojo.DomainePojo;
import org.esupportail.catapp.admin.web.utils.jsf.CustomTreeNode;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import java.util.Iterator;

import static fj.data.Array.iterableArray;
import static fj.data.Option.*;
import static javax.faces.application.FacesMessage.*;
import static org.esupportail.catapp.admin.web.utils.Transform.*;

public class DomaineController extends AbstractController<DomaineDTO, DomainePojo> {

    private TreeNode selectedNode;

    private Option<DomainePojo> parent;

    private String useCase;

    public static final String DOMAINS_URI = "/stylesheets/domaines/domaines.xhtml?faces-redirect=true";

    private DomaineController(final IDomaineService service) {
        super(service);
        this.pojo = new DomainePojo();
        this.originalPojo = new DomainePojo();
        this.parent = none();
        this.treeNode = buildTreeNode(service);
    }

    public static DomaineController domaineController(final IDomaineService service) {
        return new DomaineController(service);
    }

    public void onSelectNode(final NodeSelectEvent event) {
        final DomaineDTO data = (DomaineDTO) event.getTreeNode().getData();
        this.pojo = domaineToPojo.f(data, ((CustomTreeNode<DomaineDTO>) getTreeNode()).getSelf().flatten().toList());
        setOriginalPojo(new DomainePojo(pojo));
        this.parent = none();
    }

    public void onCreate() {
        setParent(getPojo());
        this.pojo = new DomainePojo();
    }

    public String create() {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!isUnique()) {
            facesContext.addMessage("catappform:defaultBody:codeDomaine", uniqueError);
            return null;
        }

        parent.foreach(new Effect<DomainePojo>() {
            public void e(DomainePojo p) {
                pojo.setParent(p.getCode());
            }
        });

        try {
            service.add(domaineToDTO.f(pojo));
            final String summary = resourceBundle.getString("MESSAGE.SUCCESS.ADD.DOMAIN.TITLE");
            final String detail = resourceBundle.getString("MESSAGE.SUCCESS.ADD.DOMAIN.DESCRIPTION");

            final FacesMessage facesMessage = new FacesMessage(SEVERITY_INFO, summary, detail);
            facesContext.addMessage(null, facesMessage);

            return DOMAINS_URI;
        } catch (InterruptedException e) {
            facesContext.addMessage(null, serviceUnavailableMsg);
        } catch (CrudException e) {
            final String summary = resourceBundle.getString("ERROR.SERVICE.UNABLE.ADD.DOMAIN.TITLE");
            final String detail = resourceBundle.getString("ERROR.SERVICE.UNABLE.ADD.DOMAIN.DESCRIPTION");
            final FacesMessage facesMessage = new FacesMessage(SEVERITY_ERROR, summary, detail);
            facesContext.addMessage(null, facesMessage);
        }
        return null;
    }

    public String update() {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            if (!originalPojo.getCode().equalsIgnoreCase(pojo.getCode())) {
                // le code du domaine a été modifié
                if (!isUnique()) {
                    // le code indiqué existe déjà
                    facesContext.addMessage("catappform:defaultBody:codeDomaine", uniqueError);
                    return null;
                } else {
                    // on créé un nouveau domaine avec le nouveau code
                    service.add(domaineToDTO.f(pojo));
                    // et on supprime le domaine correspondant à l'ancien code
                    service.delete(originalPojo.getCode());
                }
            } else {
                service.update(domaineToDTO.f(pojo));
            }

            final String summary = resourceBundle.getString("MESSAGE.SUCCESS.UPDATE.DOMAIN.TITLE");
            final String detail = resourceBundle.getString("MESSAGE.SUCCESS.UPDATE.DOMAIN.DESCRIPTION");

            final FacesMessage facesMessage = new FacesMessage(SEVERITY_INFO, summary, detail);
            facesContext.addMessage(null, facesMessage);

            return DOMAINS_URI;
        } catch (InterruptedException e) {
            facesContext.addMessage(null, serviceUnavailableMsg);
        } catch (CrudException e) {
            final String summary = resourceBundle.getString("ERROR.SERVICE.UNABLE.UPDATE.DOMAIN.TITLE");
            final String detail = resourceBundle.getString("ERROR.SERVICE.UNABLE.UPDATE.DOMAIN.DESCRIPTION");

            final FacesMessage facesMessage = new FacesMessage(SEVERITY_ERROR, summary, detail);
            facesContext.addMessage(null, facesMessage);
        }
        return null;
    }

    public String delete() {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            service.delete(pojo.getCode());
            final String summary = resourceBundle.getString("MESSAGE.SUCCESS.DELETE.DOMAIN.TITLE");
            final String detail = resourceBundle.getString("MESSAGE.SUCCESS.DELETE.DOMAIN.DESCRIPTION");

            final FacesMessage facesMessage = new FacesMessage(SEVERITY_INFO, summary, detail);
            facesContext.addMessage(null, facesMessage);

            return DOMAINS_URI;
        } catch (InterruptedException e) {
            facesContext.addMessage(null, serviceUnavailableMsg);
        } catch (CrudException e) {
            final String summary = resourceBundle.getString("ERROR.SERVICE.UNABLE.DELETE.DOMAIN.TITLE");
            final String detail = resourceBundle.getString("ERROR.SERVICE.UNABLE.DELETE.DOMAIN.DESCRIPTION");

            final FacesMessage facesMessage = new FacesMessage(SEVERITY_ERROR, summary, detail);
            facesContext.addMessage(null, facesMessage);
        }
        return null;
    }

    public String cancel() {
        return DOMAINS_URI;
    }

    protected boolean isUnique() {
        try {
            return !service.exists(pojo.getCode());
        } catch (InterruptedException e) {
            FacesContext.getCurrentInstance().addMessage(null, serviceUnavailableMsg);
            return false;
        }
    }

    ///////////////// JSF Accessors

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(final TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public DomainePojo getParent() {
        return parent.toNull();
    }

    public void setParent(final DomainePojo parent) {
        this.parent = fromNull(parent);
    }

    public String getUseCase() {
        return useCase;
    }

    public void setUseCase(final String useCase) {
        this.useCase = useCase;
    }

}
