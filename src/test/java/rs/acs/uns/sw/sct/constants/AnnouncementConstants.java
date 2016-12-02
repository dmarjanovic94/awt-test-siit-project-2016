package rs.acs.uns.sw.sct.constants;


import rs.acs.uns.sw.sct.comments.Comment;
import rs.acs.uns.sw.sct.marks.Mark;
import rs.acs.uns.sw.sct.realestates.Location;
import rs.acs.uns.sw.sct.realestates.RealEstate;
import rs.acs.uns.sw.sct.users.User;
import rs.acs.uns.sw.sct.util.DateUtil;

import java.sql.Timestamp;
import java.util.Set;

public interface AnnouncementConstants {

    int DB_COUNT_ANNOUNCEMENT = 2;
    int PAGE_SIZE = 2;

    Long ID = 1L;
    Double PRICE = 50D;
    String TELEPHONE_NO = "0654887612";
    String TYPE = "flat";
    RealEstate REAL_ESTATE = new RealEstate().id(1L);
    User AUTHOR = new User().id(1L);
    Timestamp DATE_ANNOUNCED = DateUtil.date("01-01-1994");
    Timestamp DATE_MODIFIED = DateUtil.date("03-01-1994");
    Timestamp EXPIRATION_DATE = DateUtil.date("20-01-1994");

    Double NEW_PRICE = 40D;
    String NEW_PHONE_NUMBER = "0654887612";
    String NEW_TYPE = "house";
    User NEW_AUTHOR = new User().id(2L);
    Timestamp NEW_DATE_ANNOUNCED = DateUtil.date("01-01-1995");
    Timestamp NEW_DATE_MODIFIED = DateUtil.date("03-01-1995");
    Timestamp NEW_EXPIRATION_DATE = DateUtil.date("20-01-1995");
    Location LOCATION = new Location().city("Novi Sad")
            .cityRegion("Grbavica")
            .country("Serbia")
            .street("Narodnog Fronta")
            .streetNumber("15");
    RealEstate NEW_REAL_ESTATE = new RealEstate().equipment("everything")
            .name("real name")
            .type("sell")
            .area(120D)
            .heatingType("central")
            .deleted(false)
            .location(LOCATION);

    Double UPDATED_PRICE = 15D;
    String UPDATED_PHONE_NUMBER = "06548812";
    String UPDATED_TYPE = "restaurant";
    RealEstate UPDATED_REAL_ESTATE = new RealEstate().id(1L);
    User UPDATED_AUTHOR = new User().id(3L);
    Timestamp UPDATED_DATE_ANNOUNCED = DateUtil.date("01-01-1996");
    Timestamp UPDATED_DATE_MODIFIED = DateUtil.date("03-01-1996");
    Timestamp UPDATED_EXPIRATION_DATE = DateUtil.date("20-01-1996");

    String DEFAULT_VERIFIED = "verified";
    Boolean DEFAULT_DELETED = false;
}
