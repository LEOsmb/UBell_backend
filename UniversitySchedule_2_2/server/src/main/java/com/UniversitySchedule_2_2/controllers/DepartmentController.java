package com.UniversitySchedule_2_2.controllers;

import static com.UniversitySchedule_2_2.constants.ResourceMappings.DEPARTMENT;

import com.UniversitySchedule_2_2.dto.DepartmentDTO;
import com.UniversitySchedule_2_2.entity.Department;
import com.UniversitySchedule_2_2.services.DepartmentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(DEPARTMENT)
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

  @GetMapping("/allDepartments")
    public List<DepartmentDTO> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

      @Override
    public boolean addExhibitAuthor(Author author, Exhibit exhibit) {
        if ((authorDao.findById(author.getId()).isPresent()) &&
                (exhibitDao.findById(exhibit.getId()).isPresent())) {
            throw new NotSavedException(ErrorMessage.EXHIBIT_AUTHOR_NOT_DELETED);
        } else {
            authorDao.addAuthor(author, exhibit);
            return true;
        }
    }

    /**
     * Method for deleting Exhibit's Author
     *
     * @param author Author which must be deleted
     * @param exhibit Exhibit for which yow want to delete Author
     * @return true if the delete was successful
     */
    @Override
    public boolean deleteExhibitAuthor(Author author, Exhibit exhibit) {
        if ((authorDao.findById(author.getId()).isPresent()) &&
                (exhibitDao.findById(exhibit.getId()).isPresent())) {
            authorDao.deleteAuthor(author, exhibit);
            return true;
        } else {
            throw new NotDeletedException(ErrorMessage.EXHIBIT_AUTHOR_NOT_DELETED);
        }
    }

    /**
     * Method for saving objects in database
     *
     * @return true if the save was successful
     */
    @Override
    public boolean save(AuthorDto authorDto) {
        if (authorDao.findByFullName(authorDto.getFirstName(), authorDto.getLastName())
                .isPresent()) {
            throw new NotSavedException(ErrorMessage.AUTHOR_NOT_SAVED);
        } else {
            Author author = new Author(authorDto.getFirstName(), authorDto.getLastName());
            authorDao.save(author);
            return true;
        }
    }

    /**
     * Method for deleting object Author by id
     *
     * @return true if the delete was successful
     */
    @Override
    public boolean deleteById(long id) {
        if (authorDao.findById(id).isPresent()) {
            authorDao.deleteById(id);
            return true;
        } else {
            throw new NotDeletedException(ErrorMessage.AUTHOR_NOT_DELETED);
        }
    }
    
    @GetMapping("/{id}")
    public DepartmentDTO getOneDepartment(@PathVariable Long id) {
        return departmentService.getOneDepartment(id);
    }

@PutMapping("/{id}")
    public void update(@RequestBody Department department, @PathVariable Long id){
  departmentService.update(id, department);
    }


    @PostMapping
  public void post(@RequestBody Department department) {
      departmentService.save(department);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id){
      departmentService.remove(id);
    }

}

