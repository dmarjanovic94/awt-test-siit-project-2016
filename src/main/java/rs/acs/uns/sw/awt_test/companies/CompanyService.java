package rs.acs.uns.sw.awt_test.companies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Company.
 */
@Service
@Transactional
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    /**
     * Save a company.
     *
     * @param company the entity to save
     * @return the persisted entity
     */
    public Company save(Company company) {
        Company result = companyRepository.save(company);
        return result;
    }

    /**
     * Get all the companies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Company> findAll(Pageable pageable) {
        Page<Company> result = companyRepository.findAll(pageable);
        return result;
    }

    /**
     * Get one company by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Company findOne(Long id) {
        Company company = companyRepository.findOne(id);
        return company;
    }

    /**
     * Delete the  company by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        companyRepository.delete(id);
    }
}
