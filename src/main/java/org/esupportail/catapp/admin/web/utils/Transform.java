package org.esupportail.catapp.admin.web.utils;

import fj.F;
import fj.F2;
import fj.data.Array;
import org.esupportail.catapp.admin.domain.enums.Activation;
import org.esupportail.catapp.admin.domain.beans.ApplicationDTO;
import org.esupportail.catapp.admin.domain.beans.DomaineDTO;
import org.esupportail.catapp.admin.web.pojo.ApplicationPojo;
import org.esupportail.catapp.admin.web.pojo.DomainePojo;

import java.util.ArrayList;
import java.util.List;

import static fj.data.Array.iterableArray;
import static fj.data.Array.single;
import static org.esupportail.catapp.admin.domain.beans.ApplicationDTO.applicationDTO;

public final class Transform {

    public static final F2<DomaineDTO, fj.data.List<DomaineDTO>, DomainePojo> domaineToPojo = new F2<DomaineDTO, fj.data.List<DomaineDTO>, DomainePojo>() {
        public DomainePojo f(final DomaineDTO dto, final fj.data.List<DomaineDTO> listDTO) {

            final Array<DomainePojo> array = iterableArray(dto.getDomains()).foldLeft(new F2<Array<DomainePojo>, String, Array<DomainePojo>>() {
                public Array<DomainePojo> f(final Array<DomainePojo> result, final String s) {
                    return listDTO.find(new F<DomaineDTO, Boolean>() {
                        public Boolean f(DomaineDTO domaineDTO) {
                            return domaineDTO.getCode().equals(s);
                        }
                    }).map(new F<DomaineDTO, Array<DomainePojo>>() {
                        public Array<DomainePojo> f(DomaineDTO domaineDTO) {
                            return result.append(single(domaineToPojo.f(domaineDTO, listDTO)));
                        }
                    }).orSome(result);
                }
            }, Array.<DomainePojo>empty());

            return new DomainePojo()
                    .withCode(dto.getCode())
                    .withLibelle(dto.getCaption())
                    .withParent(dto.getParent())
                    .withDomaines(new ArrayList<>(array.toCollection()))
                    .withApplications(dto.getApplications());
        }
    };

    public static final F<DomainePojo, DomaineDTO> domaineToDTO = new F<DomainePojo, DomaineDTO>() {
        public DomaineDTO f(final DomainePojo p) {
            final List<DomainePojo> domaines = p.getDomaines();
            final List<String> applications = p.getApplications();
            return DomaineDTO.domaineDTO(p.getCode(), p.getParent(), p.getLibelle())
                    .setDomains(iterableArray(domaines).map(new F<DomainePojo, String>() {
                        public String f(DomainePojo pojo) {
                            return pojo.getCode();
                        }
                    }).array(String[].class))
                    .setApplications(p.getApplications().toArray(new String[applications.size()]));
        }
    };


    public static final F<ApplicationDTO, ApplicationPojo> applicationToPojo = new F<ApplicationDTO, ApplicationPojo>() {
        public ApplicationPojo f(final ApplicationDTO dto) {
            return new ApplicationPojo()
                    .withCode(dto.getCode())
                    .withLibelle(dto.getCaption())
                    .withTitre(dto.getTitle())
                    .withDescription(dto.getDescription())
                    .withGroupe(dto.getGroup())
                    .withtUrl(dto.getUrl())
                    .withAccessibilite(dto.getActivation().boolValue())
                    .withDomaines(dto.getDomains());
        }
    };

    public static final F<ApplicationPojo, ApplicationDTO> applicationToDTO = new F<ApplicationPojo, ApplicationDTO>() {
        public ApplicationDTO f(final ApplicationPojo p) {
            final List<String> domaines = p.getDomaines();
            return applicationDTO(p.getCode(), p.getTitre(), p.getLibelle(), p.getDescription(), p.getUrl(),
                    p.getGroupe(), Activation.valueOf(p.isAccessibilite()), domaines.toArray(new String[domaines.size()]));
        }
    };
}
