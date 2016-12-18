package rs.acs.uns.sw.sct.comments;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import rs.acs.uns.sw.sct.SctServiceApplication;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static rs.acs.uns.sw.sct.constants.CommentConstants.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SctServiceApplication.class)
public class CommentServiceTest {
    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    private Comment newComment;
    private Comment updatedComment;
    private Comment existingComment;


    private void compareComments(Comment comm1, Comment comm2) {
        if (comm1.getId() != null && comm2.getId() != null)
            assertThat(comm1.getId()).isEqualTo(comm2.getId());
        assertThat(comm1.getContent()).isEqualTo(comm2.getContent());
        assertThat(comm1.getDate()).isEqualTo(comm2.getDate());
        assertThat(comm1.getAnnouncement().getId()).isEqualTo(comm2.getAnnouncement().getId());
        assertThat(comm1.getAuthor().getId()).isEqualTo(comm2.getAuthor().getId());
    }


    @Before
    public void initTest() {
        existingComment = new Comment()
                .id(ID)
                .content(CONTENT)
                .date(DATE)
                .announcement(ANNOUNCEMENT)
                .author(AUTHOR);
        newComment = new Comment()
                .id(null)
                .content(NEW_CONTENT)
                .date(NEW_DATE)
                .announcement(NEW_ANNOUNCEMENT)
                .author(NEW_AUTHOR);
        updatedComment = new Comment()
                .id(null)
                .content(UPDATED_CONTENT)
                .date(UPDATED_DATE)
                .announcement(UPDATED_ANNOUNCEMENT)
                .author(UPDATED_AUTHOR);
    }

    @Test
    public void testFindAllPageable() {
        PageRequest pageRequest = new PageRequest(0, PAGE_SIZE);
        Page<Comment> comments = commentRepository.findAll(pageRequest);
        assertThat(comments).hasSize(PAGE_SIZE);
    }

    @Test
    public void testFindAll() {
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(DB_COUNT_COMMENTS);
    }

    @Test
    public void testFindOne() {
        Comment comment = commentService.findOne(ID);
        assertThat(comment).isNotNull();

        compareComments(comment, existingComment);
    }

    @Test
    @Transactional
    public void testAdd() {
        int dbSizeBeforeAdd = commentRepository.findAll().size();

        Comment dbComment = commentService.save(newComment);
        assertThat(dbComment).isNotNull();

        // Validate that new comment is in the database
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(dbSizeBeforeAdd + 1);

        compareComments(dbComment, newComment);
    }

    @Test
    @Transactional
    public void testUpdate() {
        Comment dbComment = commentService.findOne(ID);

        dbComment.setAuthor(UPDATED_AUTHOR);
        dbComment.setAnnouncement(UPDATED_ANNOUNCEMENT);
        dbComment.setContent(UPDATED_CONTENT);
        dbComment.setDate(UPDATED_DATE);

        Comment updatedDbComment = commentService.save(dbComment);
        assertThat(updatedDbComment).isNotNull();

        compareComments(updatedDbComment, updatedComment);
    }

    @Test
    @Transactional
    public void testRemove() {
        int dbSizeBeforeRemove = commentRepository.findAll().size();
        commentService.delete(REMOVE_ID);

        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(dbSizeBeforeRemove - 1);

        Comment dbComment = commentService.findOne(REMOVE_ID);
        assertThat(dbComment).isNull();
    }


    /*
     * Negative tests
	 */

    @Test(expected = ConstraintViolationException.class)
    @Transactional
    public void testAddNullContent() {
        newComment.setContent(null);
        commentService.save(newComment);
        // rollback previous content
        newComment.setContent(NEW_CONTENT);
    }

    @Test(expected = ConstraintViolationException.class)
    @Transactional
    public void testAddNullDate() {
        newComment.setDate(null);
        commentService.save(newComment);
        // rollback previous date
        newComment.setDate(NEW_DATE);
    }

    @Test
    @Transactional
    public void testFindAllByAnnouncementId() {
        final Page<Comment> comments = commentService.findAllByAnnouncement(COMMENTED_ANNOUNCEMENT_ID, PAGEABLE);

        assertThat(comments).hasSize(COMMENTED_ANNOUNCEMENT_RECORDS);

        for (final Comment comment: comments) {
            assertThat(comment.getAnnouncement().getId()).isEqualTo(COMMENTED_ANNOUNCEMENT_ID);
        }
    }
}
