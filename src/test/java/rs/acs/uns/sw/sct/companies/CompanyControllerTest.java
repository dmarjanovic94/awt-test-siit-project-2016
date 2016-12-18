package rs.acs.uns.sw.sct.companies;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import rs.acs.uns.sw.sct.SctServiceApplication;
import rs.acs.uns.sw.sct.constants.CompanyConstants;
import rs.acs.uns.sw.sct.constants.UserConstants;
import rs.acs.uns.sw.sct.users.UserRepository;
import rs.acs.uns.sw.sct.util.AuthorityRoles;
import rs.acs.uns.sw.sct.util.HeaderUtil;
import rs.acs.uns.sw.sct.util.TestUtil;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rs.acs.uns.sw.sct.util.ContainsIgnoreCase.containsIgnoringCase;
import static rs.acs.uns.sw.sct.util.TestUtil.getRandomCaseInsensitiveSubstring;

/**
 * Test class for the CompanyResource REST controller.
 *
 * @see CompanyController
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SctServiceApplication.class)
public class CompanyControllerTest {

    private static final String DEFAULT_NAME = "NAME_AAA";
    private static final String UPDATED_NAME = "NAME_BBB";

    private static final String DEFAULT_ADDRESS = "ADDRESS_AAA";
    private static final String UPDATED_ADDRESS = "ADDRESS_BBB";

    private static final String DEFAULT_PHONE_NUMBER = "0600000000";
    private static final String UPDATED_PHONE_NUMBER = "0611111111";

    private static final int PAGE_SIZE = 5;

    /**
     * Company already in database, which Test Advertiser (username: 'test_advertiser_company_member) is member of
     */
    private static final long DB_TEST_COMPANY_ID = 4L;


    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc restCompanyMockMvc;

    private Company company;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createEntity() {
        return new Company()
                .name(DEFAULT_NAME)
                .address(DEFAULT_ADDRESS)
                .phoneNumber(DEFAULT_PHONE_NUMBER);
    }

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompanyController companyCtrl = new CompanyController();
        ReflectionTestUtils.setField(companyCtrl, "companyService", companyService);

        this.restCompanyMockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Before
    public void initTest() {
        company = createEntity();
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADMIN)
    public void createCompanyAsAdmin() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // Create the Company

        restCompanyMockMvc.perform(post("/api/companies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(company)))
                .andExpect(status().isCreated());

        // Validate the Company in the database
        List<Company> companies = companyRepository.findAll();
        assertThat(companies).hasSize(databaseSizeBeforeCreate + 1);
        Company testCompany = companies.get(companies.size() - 1);
        assertThat(testCompany.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCompany.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCompany.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADVERTISER)
    public void createCompanyAsAdvertiser() throws Exception {
        final int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // Create the Company

        restCompanyMockMvc.perform(post("/api/companies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(company)))
                .andExpect(status().isForbidden());

        // Validate the Company in the database
        final List<Company> companies = companyRepository.findAll();
        assertThat(companies).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void createCompanyAsGuest() throws Exception {
        final int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // Create the Company

        restCompanyMockMvc.perform(post("/api/companies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(company)))
                .andExpect(status().isUnauthorized());

        // Validate the Company in the database
        final List<Company> companies = companyRepository.findAll();
        assertThat(companies).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setName(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(company)))
                .andExpect(status().isBadRequest());

        List<Company> companies = companyRepository.findAll();
        assertThat(companies).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setAddress(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(company)))
                .andExpect(status().isBadRequest());

        List<Company> companies = companyRepository.findAll();
        assertThat(companies).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTelephoneNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setPhoneNumber(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(company)))
                .andExpect(status().isBadRequest());

        List<Company> companies = companyRepository.findAll();
        assertThat(companies).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompanies() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companies
        restCompanyMockMvc.perform(get("/api/companies?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
                .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    public void getCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get the company
        restCompanyMockMvc.perform(get("/api/companies/{id}", company.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(company.getId().intValue()))
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
                .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingCompany() throws Exception {
        // Get the company
        restCompanyMockMvc.perform(get("/api/companies/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADVERTISER, username = "test_advertiser_company_member")
    public void updateCompanyAsAdvertiser() throws Exception {
        // Initialize the database

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company
        Company updatedCompany = companyRepository.findOne(DB_TEST_COMPANY_ID);

        updatedCompany
                .name(UPDATED_NAME)
                .address(UPDATED_ADDRESS)
                .phoneNumber(UPDATED_PHONE_NUMBER);

        restCompanyMockMvc.perform(put("/api/companies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCompany)))
                .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companies = companyRepository.findAll();
        assertThat(companies).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyService.findOne(DB_TEST_COMPANY_ID);
        assertThat(testCompany.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompany.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCompany.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void updateCompanyAsGuest() throws Exception {
        // Initialize the database
        companyService.save(company);

        final int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company
        Company updatedCompany = companyRepository.findOne(company.getId());

        updatedCompany
                .name(UPDATED_NAME)
                .address(UPDATED_ADDRESS)
                .phoneNumber(UPDATED_PHONE_NUMBER);

        restCompanyMockMvc.perform(put("/api/companies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCompany)))
                .andExpect(status().isUnauthorized());

        // Validate the Company in the database
        final List<Company> companies = companyRepository.findAll();
        assertThat(companies).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADMIN)
    public void deleteCompanyAsAdmin() throws Exception {
        // Initialize the database
        companyService.save(company);

        int databaseSizeBeforeDelete = companyRepository.findAll().size();

        // Get the company
        restCompanyMockMvc.perform(delete("/api/companies/{id}", company.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Company> companies = companyRepository.findAll();
        assertThat(companies).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADVERTISER)
    public void deleteCompanyAsAdvertiser() throws Exception {
        // Initialize the database
        companyService.save(company);

        final int databaseSizeBeforeDelete = companyRepository.findAll().size();

        // Get the company
        restCompanyMockMvc.perform(delete("/api/companies/{id}", company.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());

        // Validate the database is empty
        List<Company> companies = companyRepository.findAll();
        assertThat(companies).hasSize(databaseSizeBeforeDelete);
    }

    @Test
    @Transactional
    public void deleteCompanyAsGuest() throws Exception {
        // Initialize the database
        companyService.save(company);

        final int databaseSizeBeforeDelete = companyRepository.findAll().size();

        // Get the company
        restCompanyMockMvc.perform(delete("/api/companies/{id}", company.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized());

        // Validate the database is empty
        List<Company> companies = companyRepository.findAll();
        assertThat(companies).hasSize(databaseSizeBeforeDelete);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADVERTISER, username = UserConstants.ADVERTISER_USERNAME)
    public void sendRequestForCompanyAsAdvertiser() throws Exception {
        // Initialize the database
        companyService.save(company);

        restCompanyMockMvc.perform(put("/api/companies/{companyId}/user-request/", company.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.company.id").value(company.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADMIN, username = UserConstants.ADVERTISER_USERNAME)
    public void sendRequestForCompanyAsAdmin() throws Exception {
        // Initialize the database
        companyService.save(company);

        restCompanyMockMvc.perform(put("/api/companies/{companyId}/user-request/", company.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    public void sendRequestForCompanyAsGuest() throws Exception {
        // Initialize the database
        companyService.save(company);

        restCompanyMockMvc.perform(put("/api/companies/{companyId}/user-request/", company.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADVERTISER, username = "test_advertiser_company_member")
    public void sendRequestForCompanyAsAlreadyCompanyVerifiedAdvertiser() throws Exception {
        // Initialize the database
        companyService.save(company);

        final MvcResult result = restCompanyMockMvc.perform(put("/api/companies/{companyId}/user-request/", company.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isConflict())
                .andReturn();

        final String message = result.getResponse().getContentAsString();
        final Integer errorKey = Integer.valueOf(result.getResponse().getHeader(HeaderUtil.SCT_HEADER_ERROR_KEY));
        assertThat(errorKey).isEqualTo(HeaderUtil.ERROR_CODE_ALREADY_REQUESTED_MEMBERSHIP);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADVERTISER, username = UserConstants.ADVERTISER_COMPANY_USERNAME)
    public void sendRequestForCompanyAsAlreadyCompanyVerifiedAdvertiserAndOverrideCompany() throws Exception {
        // Initialize the database
        companyService.save(company);

        restCompanyMockMvc.perform(put("/api/companies/{companyId}/user-request/", company.getId())
                .param("confirmed", "true")
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.company.id").value(company.getId()));
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADVERTISER, username = UserConstants.ADVERTISER_COMPANY_USERNAME)
    public void getCompanyRequestsByStatusAsRightCompanyVerifiedAdvertiser() throws Exception {

        final String status = "accepted";

        // Already Existed Company
        final Long companyId = 1L;

        final Long count = userRepository.findByCompany_IdAndCompanyVerified(companyId, status, CompanyConstants.PAGEABLE).getTotalElements();

        restCompanyMockMvc.perform(get("/api/companies/users-requests", companyId)
                .param("status", status)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(Math.toIntExact(count))));
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADVERTISER, username = "test_advertiser_pending_membership")
    public void getCompanyRequestsByStatusAsCompanyNotVerifiedAdvertiser() throws Exception {

        final String status = "accepted";

        // Already Existed Company
        final Long companyId = 4L;

        final MvcResult result = restCompanyMockMvc.perform(get("/api/companies/users-requests", companyId)
                .param("status", status)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();

        final String message = result.getResponse().getContentAsString();
        final Integer errorKey = Integer.valueOf(result.getResponse().getHeader(HeaderUtil.SCT_HEADER_ERROR_KEY));
        assertThat(errorKey).isEqualTo(HeaderUtil.ERROR_CODE_NOT_MEMBER_OF_COMPANY);
    }

    @Test
    @Transactional
    public void getCompanyRequestsByStatusAsGuest() throws Exception {

        final String status = "accepted";

        // Already Existed Company
        final Long companyId = 1L;

        restCompanyMockMvc.perform(get("/api/companies/users-requests", companyId)
                .param("status", status)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADMIN)
    public void getCompanyRequestsByStatusAsAdmin() throws Exception {

        final String status = "accepted";

        // Already Existed Company
        final Long companyId = 1L;

        restCompanyMockMvc.perform(get("/api/companies/users-requests", companyId)
                .param("status", status)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    public void resolveCompanyMembershipAsGuest() throws Exception {

        final String accepted = "true";

        // Already Existed Company
        final Long userId = 1L;

        restCompanyMockMvc.perform(put("/api/companies/resolve-request/user/{userId}", userId)
                .param("accepted", accepted)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADVERTISER, username = "test_advertiser_company_member")
    public void resolveCompanyMembershipAsAdvertiserForWrongUser() throws Exception {

        final String accepted = "true";

        // Already Existed Company
        final Long userId = Long.MAX_VALUE;

        final MvcResult result = restCompanyMockMvc.perform(put("/api/companies/resolve-request/user/{userId}", userId)
                .param("accepted", accepted)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andReturn();

        final Integer errorKey = Integer.valueOf(result.getResponse().getHeader(HeaderUtil.SCT_HEADER_ERROR_KEY));
        assertThat(errorKey).isEqualTo(HeaderUtil.ERROR_CODE_NON_EXISTING_ENTITY);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADVERTISER, username = "test_advertiser_company_member")
    public void resolveCompanyMembershipAsAdvertiserForUserWithoutRequest() throws Exception {

        final String accepted = "true";

        // Already Existed Company
        final Long userId = 14L;

        final MvcResult result = restCompanyMockMvc.perform(put("/api/companies/resolve-request/user/{userId}", userId)
                .param("accepted", accepted)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotAcceptable())
                .andReturn();

        final Integer errorKey = Integer.valueOf(result.getResponse().getHeader(HeaderUtil.SCT_HEADER_ERROR_KEY));
        assertThat(errorKey).isEqualTo(HeaderUtil.ERROR_CODE_USER_DID_NOT_REQUEST_MEMBERSHIP);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADVERTISER, username = "test_advertiser_company_member")
    public void resolveCompanyMembershipAsVerifierWithAlreadyAcceptedUser() throws Exception {

        final String accepted = "true";

        // User that's already in the same company
        final Long userId = 16L;

        final MvcResult result = restCompanyMockMvc.perform(put("/api/companies/resolve-request/user/{userId}", userId)
                .param("accepted", accepted)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotAcceptable())
                .andReturn();

        final Integer errorKey = Integer.valueOf(result.getResponse().getHeader(HeaderUtil.SCT_HEADER_ERROR_KEY));

        assertThat(errorKey).isEqualTo(HeaderUtil.ERROR_CODE_USER_DID_NOT_REQUEST_MEMBERSHIP);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADVERTISER, username = "test_advertiser_other_company_member")
    public void resolveCompanyMembershipAsAdvertiserForUserWithDifferentCompany() throws Exception {

        // Our user belongs to company with ID = 1

        final String accepted = "true";

        // User which pending for company with ID = 3
        final Long userId = 13L;

        final MvcResult result = restCompanyMockMvc.perform(put("/api/companies/resolve-request/user/{userId}", userId)
                .param("accepted", accepted)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();

        final Integer errorKey = Integer.valueOf(result.getResponse().getHeader(HeaderUtil.SCT_HEADER_ERROR_KEY));
        assertThat(errorKey).isEqualTo(HeaderUtil.ERROR_CODE_NO_PERMISSION_TO_RESOLVE_MEMBERSHIP);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADVERTISER, username = "test_advertiser_company_member")
    public void resolveCompanyMembershipAsAdvertiserAndSetToAccepted() throws Exception {

        // Our user belongs to company with ID = 4

        final String accepted = "true";

        // User which pending for company with ID = 4
        final Long userId = 13L;

        restCompanyMockMvc.perform(put("/api/companies/resolve-request/user/{userId}", userId)
                .param("accepted", accepted)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // TODO Check for updated user
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADVERTISER, username = "test_advertiser_company_member")
    public void resolveCompanyMembershipAsAdvertiserAndSetToFalse() throws Exception {

        // Our user belongs to company with ID = 4

        final String accepted = "false";

        // User which pending for company with ID = 4
        final Long userId = 13L;

        restCompanyMockMvc.perform(put("/api/companies/resolve-request/user/{userId}", userId)
                .param("accepted", accepted)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());


        // TODO Check for updated user
    }


    @Test
    @Transactional
    public void searchCompaniesWithoutAnyAttribute() throws Exception {
        final int dbSize = companyRepository.findAll().size();
        final int requiredSize = dbSize < PAGE_SIZE ? dbSize : PAGE_SIZE;

        restCompanyMockMvc.perform(get("/api/companies/search")
                .param("sort", "id,desc")
                .param("size", String.valueOf(PAGE_SIZE))
                .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(Math.toIntExact(requiredSize))))
                .andReturn();
    }

    @Test
    @Transactional
    public void searchCompaniesByNameAndAddressAndPhoneNumber() throws Exception {
        // prepare db data
        companyService.save(company);

        final String randomName = getRandomCaseInsensitiveSubstring(company.getName());
        final String randomAddress = getRandomCaseInsensitiveSubstring(company.getAddress());
        final String randomPhoneNumber = getRandomCaseInsensitiveSubstring(company.getPhoneNumber());

        restCompanyMockMvc.perform(get("/api/companies/search")
                .param("sort", "id,desc")
                .param("size", String.valueOf(PAGE_SIZE))
                .param("page", "0")
                .param("name", randomName)
                .param("address", randomAddress)
                .param("phoneNumber", randomPhoneNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].name", everyItem(containsIgnoringCase(randomName))))
                .andExpect(jsonPath("$.[*].name", hasItem(company.getName())))
                .andExpect(jsonPath("$.[*].address", everyItem(containsIgnoringCase(randomAddress))))
                .andExpect(jsonPath("$.[*].address", hasItem(company.getAddress())))
                .andExpect(jsonPath("$.[*].phoneNumber", everyItem(containsIgnoringCase(randomPhoneNumber))))
                .andExpect(jsonPath("$.[*].phoneNumber", hasItem(company.getPhoneNumber())))
                .andReturn();
    }
}
