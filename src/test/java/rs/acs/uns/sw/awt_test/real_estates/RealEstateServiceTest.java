package rs.acs.uns.sw.awt_test.real_estates;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import rs.acs.uns.sw.awt_test.AwtTestSiitProject2016Application;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static rs.acs.uns.sw.awt_test.constants.RealEstateConstants.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AwtTestSiitProject2016Application.class)
public class RealEstateServiceTest {

    @Autowired
    private RealEstateService realEstateService;

    @Autowired
    private RealEstateRepository realEstateRepository;

    private RealEstate newRealEstate;
    private RealEstate updatedRealEstate;
    private RealEstate existingRealEstate;

    private void compareRealEstate(RealEstate re1, RealEstate re2){
        if (re1.getId() != null && re2.getId() != null)
            assertThat(re1.getId()).isEqualTo(re2.getId());
        assertThat(re1.getArea()).isEqualTo(re2.getArea());
        assertThat(re1.getHeatingType()).isEqualTo(re2.getHeatingType());
        assertThat(re1.getName()).isEqualTo(re2.getName());
        assertThat(re1.getType()).isEqualTo(re2.getType());
    }


    @Before
    public void initTest() {
        existingRealEstate = new RealEstate(ID, NAME, TYPE, AREA, HEATING_TYPE, DEFAULT_ANNOUNCEMENTS, DEFAULT_DELETED);
        newRealEstate = new RealEstate(null, NEW_NAME, NEW_TYPE, NEW_AREA, NEW_HEATING_TYPE, DEFAULT_ANNOUNCEMENTS, DEFAULT_DELETED);
        updatedRealEstate = new RealEstate(null, UPDATED_NAME, UPDATED_TYPE, UPDATED_AREA, UPDATED_HEATING_TYPE, DEFAULT_ANNOUNCEMENTS, DEFAULT_DELETED);
    }

    @Test
    public void testFindAllPageable() {
        PageRequest pageRequest = new PageRequest(0, PAGE_SIZE);
        Page<RealEstate> realEstates = realEstateService.findAll(pageRequest);
        assertThat(realEstates).hasSize(PAGE_SIZE);
    }

    @Test
    public void testFindAll() {
        List<RealEstate> realEstateList = realEstateRepository.findAll();
        assertThat(realEstateList).hasSize(DB_COUNT_REAL_ESTATES);
    }

    @Test
    public void testFindOne(){
        RealEstate realEstate = realEstateService.findOne(ID);
        assertThat(realEstate).isNotNull();

        compareRealEstate(realEstate, existingRealEstate);
    }

    @Test
    @Transactional
    public void testAdd() {
        int dbSizeBeforeAdd = realEstateRepository.findAll().size();

        RealEstate dbRealEstate = realEstateService.save(newRealEstate);
        assertThat(dbRealEstate).isNotNull();

        // Validate that new real estate is in the database
        List<RealEstate> realEstates = realEstateRepository.findAll();
        assertThat(realEstates).hasSize(dbSizeBeforeAdd + 1);

        compareRealEstate(dbRealEstate, newRealEstate);
    }


    @Test
    @Transactional
    public void testUpdate() {
        RealEstate dbRealEstate = realEstateService.findOne(ID);

        dbRealEstate.setArea(UPDATED_AREA);
        dbRealEstate.setHeatingType(UPDATED_HEATING_TYPE);
        dbRealEstate.setName(UPDATED_NAME);
        dbRealEstate.setType(UPDATED_TYPE);

        RealEstate updatedDbRealEstate = realEstateService.save(dbRealEstate);
        assertThat(updatedDbRealEstate).isNotNull();

        compareRealEstate(updatedDbRealEstate, updatedRealEstate);
    }

    @Test
    @Transactional
    public void testRemove() {
        int dbSizeBeforeRemove = realEstateRepository.findAll().size();
        realEstateService.delete(REMOVE_ID);

        List<RealEstate> realEstates = realEstateRepository.findAll();
        assertThat(realEstates).hasSize(dbSizeBeforeRemove - 1);

        RealEstate dbRealEstate = realEstateService.findOne(REMOVE_ID);
        assertThat(dbRealEstate).isNull();
    }

    /*
	 * Negative tests
	 */

    @Test(expected = ConstraintViolationException.class)
    @Transactional
    public void testAddNullName() {
        newRealEstate.setName(null);
        realEstateService.save(newRealEstate);
        // rollback previous name
        newRealEstate.setName(NEW_NAME);
    }

    @Test(expected = ConstraintViolationException.class)
    @Transactional
    public void testAddNullType() {
        newRealEstate.setType(null);
        realEstateService.save(newRealEstate);
        // rollback previous type
        newRealEstate.setType(NEW_TYPE);
    }

    @Test(expected = ConstraintViolationException.class)
    @Transactional
    public void testAddNullArea() {
        newRealEstate.setArea(null);
        realEstateService.save(newRealEstate);
        // rollback previous area
        newRealEstate.setArea(NEW_AREA);
    }

    @Test(expected = ConstraintViolationException.class)
    @Transactional
    public void testAddNullHeatingType() {
        newRealEstate.setHeatingType(null);
        realEstateService.save(newRealEstate);
        // rollback previous heating type
        newRealEstate.setHeatingType(NEW_HEATING_TYPE);
    }
}
