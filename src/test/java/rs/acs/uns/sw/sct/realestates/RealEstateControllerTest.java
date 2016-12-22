package rs.acs.uns.sw.sct.realestates;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import rs.acs.uns.sw.sct.SctServiceApplication;
import rs.acs.uns.sw.sct.constants.RealEstateConstants;
import rs.acs.uns.sw.sct.util.AuthorityRoles;
import rs.acs.uns.sw.sct.util.TestUtil;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RealEstate REST controller.
 *
 * @see RealEstateController
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SctServiceApplication.class)
public class RealEstateControllerTest {

    private static final String DEFAULT_NAME = "NAME_AAA";
    private static final String UPDATED_NAME = "NAME_BBB";

    private static final String DEFAULT_TYPE = "TYPE_AAA";
    private static final String UPDATED_TYPE = "TYPE_BBB";

    private static final Double DEFAULT_AREA = 1D;
    private static final Double UPDATED_AREA = 2D;

    private static final String DEFAULT_HEATING_TYPE = "HEATING_AAA";
    private static final String UPDATED_HEATING_TYPE = "HEATING_BBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    @Autowired
    private RealEstateRepository realEstateRepository;

    @Autowired
    private RealEstateService realEstateService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc restRealEstateMockMvc;

    private RealEstate realEstate;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RealEstate createEntity() {
        return new RealEstate()
                .name(DEFAULT_NAME)
                .type(DEFAULT_TYPE)
                .area(DEFAULT_AREA)
                .heatingType(DEFAULT_HEATING_TYPE)
                .deleted(DEFAULT_DELETED);
    }

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RealEstateController realEstateCtrl = new RealEstateController();
        ReflectionTestUtils.setField(realEstateCtrl, "realEstateService", realEstateService);
        this.restRealEstateMockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Before
    public void initTest() {
        realEstate = createEntity();
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADVERTISER)
    public void createRealEstateAsAdvertiser() throws Exception {
        int databaseSizeBeforeCreate = realEstateRepository.findAll().size();

        // Create the RealEstate

        restRealEstateMockMvc.perform(post("/api/real-estates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(realEstate)))
                .andExpect(status().isCreated());

        // Validate the RealEstate in the database
        List<RealEstate> realEstates = realEstateRepository.findAll();
        assertThat(realEstates).hasSize(databaseSizeBeforeCreate + 1);
        RealEstate testRealEstate = realEstates.get(realEstates.size() - 1);
        assertThat(testRealEstate.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRealEstate.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testRealEstate.getArea()).isEqualTo(DEFAULT_AREA);
        assertThat(testRealEstate.getHeatingType()).isEqualTo(DEFAULT_HEATING_TYPE);
        assertThat(testRealEstate.isDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADMIN)
    public void createRealEstateAsAdmin() throws Exception {
        int databaseSizeBeforeCreate = realEstateRepository.findAll().size();

        restRealEstateMockMvc.perform(post("/api/real-estates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(realEstate)))
                .andExpect(status().isForbidden());

        // Validate the RealEstate in the database
        List<RealEstate> realEstates = realEstateRepository.findAll();
        assertThat(realEstates).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void createRealEstateAsGuest() throws Exception {
        int databaseSizeBeforeCreate = realEstateRepository.findAll().size();

        restRealEstateMockMvc.perform(post("/api/real-estates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(realEstate)))
                .andExpect(status().isUnauthorized());

        // Validate the RealEstate in the database
        List<RealEstate> realEstates = realEstateRepository.findAll();
        assertThat(realEstates).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = realEstateRepository.findAll().size();
        // set the field null
        realEstate.setName(null);

        // Create the RealEstate, which fails.

        restRealEstateMockMvc.perform(post("/api/real-estates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(realEstate)))
                .andExpect(status().isBadRequest());

        List<RealEstate> realEstates = realEstateRepository.findAll();
        assertThat(realEstates).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = realEstateRepository.findAll().size();
        // set the field null
        realEstate.setType(null);

        // Create the RealEstate, which fails.

        restRealEstateMockMvc.perform(post("/api/real-estates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(realEstate)))
                .andExpect(status().isBadRequest());

        List<RealEstate> realEstates = realEstateRepository.findAll();
        assertThat(realEstates).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAreaIsRequired() throws Exception {
        int databaseSizeBeforeTest = realEstateRepository.findAll().size();
        // set the field null
        realEstate.setArea(null);

        // Create the RealEstate, which fails.

        restRealEstateMockMvc.perform(post("/api/real-estates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(realEstate)))
                .andExpect(status().isBadRequest());

        List<RealEstate> realEstates = realEstateRepository.findAll();
        assertThat(realEstates).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHeatingTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = realEstateRepository.findAll().size();
        // set the field null
        realEstate.setHeatingType(null);

        // Create the RealEstate, which fails.

        restRealEstateMockMvc.perform(post("/api/real-estates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(realEstate)))
                .andExpect(status().isBadRequest());

        List<RealEstate> realEstates = realEstateRepository.findAll();
        assertThat(realEstates).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = realEstateRepository.findAll().size();
        // set the field null
        realEstate.setDeleted(null);

        // Create the RealEstate, which fails.

        restRealEstateMockMvc.perform(post("/api/real-estates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(realEstate)))
                .andExpect(status().isBadRequest());

        List<RealEstate> realEstates = realEstateRepository.findAll();
        assertThat(realEstates).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADMIN)
    public void getAllRealEstatesAsAdmin() throws Exception {
        // Initialize the database
        realEstateRepository.saveAndFlush(realEstate);

        // Get all the realEstates
        restRealEstateMockMvc.perform(get("/api/real-estates?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(realEstate.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
                .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA)))
                .andExpect(jsonPath("$.[*].heatingType").value(hasItem(DEFAULT_HEATING_TYPE)))
                .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)));
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADVERTISER)
    public void getAllRealEstatesAsAdvertiser() throws Exception {
        // Initialize the database
        realEstateRepository.saveAndFlush(realEstate);

        // Get all the realEstates
        restRealEstateMockMvc.perform(get("/api/real-estates?sort=id,desc"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    public void getAllRealEstatesAsGuest() throws Exception {
        // Initialize the database
        realEstateRepository.saveAndFlush(realEstate);

        // Get all the realEstates
        restRealEstateMockMvc.perform(get("/api/real-estates?sort=id,desc"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADMIN)
    public void getRealEstate() throws Exception {
        // Initialize the database
        realEstateRepository.saveAndFlush(realEstate);

        // Get the realEstate
        restRealEstateMockMvc.perform(get("/api/real-estates/{id}", realEstate.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(realEstate.getId().intValue()))
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
                .andExpect(jsonPath("$.area").value(DEFAULT_AREA))
                .andExpect(jsonPath("$.heatingType").value(DEFAULT_HEATING_TYPE))
                .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED));
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADMIN)
    public void getNonExistingRealEstate() throws Exception {
        // Get the realEstate
        restRealEstateMockMvc.perform(get("/api/real-estates/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADVERTISER)
    public void updateRealEstate() throws Exception {
        // Initialize the database
        realEstateService.save(realEstate);

        int databaseSizeBeforeUpdate = realEstateRepository.findAll().size();

        // Update the realEstate
        RealEstate updatedRealEstate = realEstateRepository.findOne(realEstate.getId());
        updatedRealEstate
                .name(UPDATED_NAME)
                .type(UPDATED_TYPE)
                .area(UPDATED_AREA)
                .heatingType(UPDATED_HEATING_TYPE)
                .deleted(UPDATED_DELETED);

        restRealEstateMockMvc.perform(put("/api/real-estates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRealEstate)))
                .andExpect(status().isOk());

        // Validate the RealEstate in the database
        List<RealEstate> realEstates = realEstateRepository.findAll();
        assertThat(realEstates).hasSize(databaseSizeBeforeUpdate);
        RealEstate testRealEstate = realEstates.get(realEstates.size() - 1);
        assertThat(testRealEstate.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRealEstate.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testRealEstate.getArea()).isEqualTo(UPDATED_AREA);
        assertThat(testRealEstate.getHeatingType()).isEqualTo(UPDATED_HEATING_TYPE);
        assertThat(testRealEstate.isDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateRealEstateAsGuest() throws Exception {
        // Initialize the database
        realEstateService.save(realEstate);

        final int databaseSizeBeforeUpdate = realEstateRepository.findAll().size();

        // Update the realEstate
        RealEstate updatedRealEstate = realEstateRepository.findOne(realEstate.getId());
        updatedRealEstate
                .name(UPDATED_NAME)
                .type(UPDATED_TYPE)
                .area(UPDATED_AREA)
                .heatingType(UPDATED_HEATING_TYPE)
                .deleted(UPDATED_DELETED);

        restRealEstateMockMvc.perform(put("/api/real-estates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRealEstate)))
                .andExpect(status().isUnauthorized());

        // Validate the RealEstate in the database
        final List<RealEstate> realEstates = realEstateRepository.findAll();
        assertThat(realEstates).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADMIN)
    public void deleteRealEstateAsAdmin() throws Exception {
        // Initialize the database
        realEstateService.save(realEstate);

        int databaseSizeBeforeDelete = realEstateRepository.findAll().size();

        // Get the realEstate
        restRealEstateMockMvc.perform(delete("/api/real-estates/{id}", realEstate.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<RealEstate> realEstates = realEstateRepository.findAll();
        assertThat(realEstates).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.VERIFIER)
    public void deleteRealEstateAsVerifier() throws Exception {
        // Initialize the database
        realEstateService.save(realEstate);

        int databaseSizeBeforeDelete = realEstateRepository.findAll().size();

        // Get the realEstate
        restRealEstateMockMvc.perform(delete("/api/real-estates/{id}", realEstate.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());

        // Validate the database is empty
        List<RealEstate> realEstates = realEstateRepository.findAll();
        assertThat(realEstates).hasSize(databaseSizeBeforeDelete);
    }


    @Test
    @Transactional
    public void deleteRealEstateAsGuest() throws Exception {
        // Initialize the database
        realEstateService.save(realEstate);

        int databaseSizeBeforeDelete = realEstateRepository.findAll().size();

        // Get the realEstate
        restRealEstateMockMvc.perform(delete("/api/real-estates/{id}", realEstate.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized());

        // Validate the database is empty
        List<RealEstate> realEstates = realEstateRepository.findAll();
        assertThat(realEstates).hasSize(databaseSizeBeforeDelete);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADMIN)
    public void getAllDeletedRealEstatesAsAdmin() throws Exception {

        final boolean REAL_ESTATE_DELETED = true;

        realEstate.deleted(REAL_ESTATE_DELETED);

        // Add Deleted Announcement
        realEstateRepository.saveAndFlush(realEstate);

        final Long count = realEstateRepository.findAllByDeleted(REAL_ESTATE_DELETED, RealEstateConstants.PAGEABLE).getTotalElements();

        // Get all non deleted announcements
        restRealEstateMockMvc.perform(get("/api/real-estates/deleted/{status}", REAL_ESTATE_DELETED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(Math.toIntExact(count))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(realEstate.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
                .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA)))
                .andExpect(jsonPath("$.[*].heatingType").value(hasItem(DEFAULT_HEATING_TYPE)))
                .andExpect(jsonPath("$.[*].deleted").value(hasItem(REAL_ESTATE_DELETED)));
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADVERTISER)
    public void getAllNonDeletedRealEstatesAsAdvertiser() throws Exception {

        final boolean REAL_ESTATE_DELETED = false;

        // Add Deleted Announcement
        realEstateRepository.saveAndFlush(realEstate);

        final Long count = realEstateRepository.findAllByDeleted(REAL_ESTATE_DELETED, RealEstateConstants.PAGEABLE).getTotalElements();

        // Get all non deleted announcements
        restRealEstateMockMvc.perform(get("/api/real-estates/deleted/{status}", REAL_ESTATE_DELETED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(Math.toIntExact(count))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(realEstate.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
                .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA)))
                .andExpect(jsonPath("$.[*].heatingType").value(hasItem(DEFAULT_HEATING_TYPE)))
                .andExpect(jsonPath("$.[*].deleted").value(hasItem(REAL_ESTATE_DELETED)));
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADVERTISER)
    public void getAllSimilarRealEstatesAsAdvertiser() throws Exception {

        Location location = new Location()
                .country(RealEstateConstants.SIMILAR_COUNTRY)
                .city(RealEstateConstants.SIMILAR_CITY)
                .cityRegion(RealEstateConstants.SIMILAR_REGION)
                .street(RealEstateConstants.SIMILAR_STREET)
                .streetNumber(RealEstateConstants.SIMILAR_STREET_NO);

        final RealEstateSimilarDTO similar = new RealEstateSimilarDTO(location, Double.parseDouble(RealEstateConstants.SIMILAR_AREA));

        final Long count = realEstateService.findAllSimilar(similar, RealEstateConstants.PAGEABLE).getTotalElements();

        restRealEstateMockMvc.perform(get("/api/real-estates/similar")
                .param("area", RealEstateConstants.SIMILAR_AREA)
                .param("country", RealEstateConstants.SIMILAR_COUNTRY)
                .param("city", RealEstateConstants.SIMILAR_CITY)
                .param("region", RealEstateConstants.SIMILAR_REGION)
                .param("street", RealEstateConstants.SIMILAR_STREET)
                .param("number", RealEstateConstants.SIMILAR_STREET_NO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(Math.toIntExact(count))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADVERTISER)
    public void getAllSimilarRealEstatesAsAdvertiserGoodAddressWrongArea() throws Exception {

        final String NOT_SIMILAR_AREA = "2110994";

        Location location = new Location()
                .country(RealEstateConstants.SIMILAR_COUNTRY)
                .city(RealEstateConstants.SIMILAR_CITY)
                .cityRegion(RealEstateConstants.SIMILAR_REGION)
                .street(RealEstateConstants.SIMILAR_STREET)
                .streetNumber(RealEstateConstants.SIMILAR_STREET_NO);

        final RealEstateSimilarDTO similar = new RealEstateSimilarDTO(location, Double.parseDouble(NOT_SIMILAR_AREA));

        final Long count = realEstateService.findAllSimilar(similar, RealEstateConstants.PAGEABLE).getTotalElements();

        // We wanted empty result
        assertThat(count).isEqualTo(0);

        restRealEstateMockMvc.perform(get("/api/real-estates/similar")
                .param("area", NOT_SIMILAR_AREA)
                .param("country", RealEstateConstants.SIMILAR_COUNTRY)
                .param("city", RealEstateConstants.SIMILAR_CITY)
                .param("region", RealEstateConstants.SIMILAR_REGION)
                .param("street", RealEstateConstants.SIMILAR_STREET)
                .param("number", RealEstateConstants.SIMILAR_STREET_NO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(Math.toIntExact(count))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.ADVERTISER)
    public void getAllSimilarRealEstatesAsAdvertiserGoodAreaWrongAddress() throws Exception {

        final String NOT_SIMILAR_CITY = "Bijeljina";

        Location location = new Location()
                .country(RealEstateConstants.SIMILAR_COUNTRY)
                .city(NOT_SIMILAR_CITY)
                .cityRegion(RealEstateConstants.SIMILAR_REGION)
                .street(RealEstateConstants.SIMILAR_STREET)
                .streetNumber(RealEstateConstants.SIMILAR_STREET_NO);

        final RealEstateSimilarDTO similar = new RealEstateSimilarDTO(location, Double.parseDouble(RealEstateConstants.SIMILAR_AREA));

        final Long count = realEstateService.findAllSimilar(similar, RealEstateConstants.PAGEABLE).getTotalElements();

        // We wanted empty result
        assertThat(count).isEqualTo(0);

        restRealEstateMockMvc.perform(get("/api/real-estates/similar")
                .param("area", RealEstateConstants.SIMILAR_AREA)
                .param("country", RealEstateConstants.SIMILAR_COUNTRY)
                .param("city", NOT_SIMILAR_CITY)
                .param("region", RealEstateConstants.SIMILAR_REGION)
                .param("street", RealEstateConstants.SIMILAR_STREET)
                .param("number", RealEstateConstants.SIMILAR_STREET_NO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(Math.toIntExact(count))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthorityRoles.VERIFIER)
    public void getAllSimilarRealEstatesAsVerifier() throws Exception {

        restRealEstateMockMvc.perform(get("/api/real-estates/similar")
                .param("area", RealEstateConstants.SIMILAR_AREA)
                .param("country", RealEstateConstants.SIMILAR_COUNTRY)
                .param("city", RealEstateConstants.SIMILAR_CITY)
                .param("region", RealEstateConstants.SIMILAR_REGION)
                .param("street", RealEstateConstants.SIMILAR_STREET)
                .param("number", RealEstateConstants.SIMILAR_STREET_NO))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    public void getAllSimilarRealEstatesAsGuest() throws Exception {

        restRealEstateMockMvc.perform(get("/api/real-estates/similar")
                .param("area", RealEstateConstants.SIMILAR_AREA)
                .param("country", RealEstateConstants.SIMILAR_COUNTRY)
                .param("city", RealEstateConstants.SIMILAR_CITY)
                .param("region", RealEstateConstants.SIMILAR_REGION)
                .param("street", RealEstateConstants.SIMILAR_STREET)
                .param("number", RealEstateConstants.SIMILAR_STREET_NO))
                .andExpect(status().isUnauthorized());
    }
}