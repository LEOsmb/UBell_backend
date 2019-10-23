package com.UniversitySchedule_2_2.controllers;

import static com.UniversitySchedule_2_2.constants.ResourceMappings.STUDENT;

import com.UniversitySchedule_2_2.dto.RankDTO;
import com.UniversitySchedule_2_2.dto.StudentDTO;
import com.UniversitySchedule_2_2.entity.Rank;
import com.UniversitySchedule_2_2.entity.Student;
import com.UniversitySchedule_2_2.services.StudentService;
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
@RequestMapping(STUDENT)
public class StudentController {

    @Autowired
    private StudentService studentService;

  @GetMapping("/allStudents")
    public List<StudentDTO> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public StudentDTO getOneStudent(@PathVariable Long id) {
        return studentService.getOneStudent(id);
    }

@PutMapping("/{id}")
    public void update(@RequestBody Student student, @PathVariable Long id){
  studentService.update(id, student);
    }
    
    /**
     * Method for saving object Exhibit in database
     *
     * @return true if the save was successful
     */
    @Override
    public boolean save(ExhibitDto exhibitDto) {
        if (exhibitDao.findByName(exhibitDto.getName()).isPresent()) {
            throw new NotSavedException(ErrorMessage.EXHIBIT_NOT_SAVED);
        } else {
            Exhibit exhibit = new Exhibit(
                    exhibitDto.getType(),
                    exhibitDto.getMaterial(),
                    exhibitDto.getTechnique(),
                    exhibitDto.getName());

            exhibitDao.save(exhibit);
            Long exhibitId = exhibitDao.findByName(exhibit.getName()).get().getId();
            exhibit.setId(exhibitId);

            Audience audience = new Audience();
            audience.setId(exhibitDto.getAudienceId());

            exhibitDao.updateAudience(exhibit, audience);

            for (Long id : exhibitDto.getAuthorsId()) {
                Author author = new Author();
                author.setId(id);
                exhibitDao.addAuthor(exhibit, author);
            }
            return true;
        }
    }

    /**
     * Method for deleting object by id
     *
     * @return true if the delete was successful
     */
    @Override
    public boolean deleteById(long id) {
        if (exhibitDao.findById(id).isPresent()) {
            exhibitDao.deleteById(id);
            return true;
        } else {
            throw new NotDeletedException(ErrorMessage.EXHIBIT_NOT_DELETED);
        }
    }

    /**
     * Method for deleting object Exhibit by id
     *
     * @return true if the delete was successful
     */
    @Override
    public Optional<Exhibit> findById(long id) {
        Exhibit exhibit = exhibitDao.findById(id).orElse(null);
        return Optional.of(Optional.of(exhibitDao.loadForeignFields(exhibit))
                .orElseThrow(() -> new NotFoundException(ErrorMessage.OBJECT_NOT_FOUND)));
    }

    /**
     * Method for finding Exhibit by name
     *
     * @param name of Exhibit
     * @return Exhibit object wrapped in Optional
     */
    @Override
    public Optional<Exhibit> findByName(String name) {
        Exhibit exhibit = exhibitDao.findByName(name).orElse(null);
        return Optional.of(Optional.of(exhibitDao.loadForeignFields(exhibit))
                .orElseThrow(() -> new NotFoundException(ErrorMessage.OBJECT_NOT_FOUND)));
    }

    /**
     * Method, that returns all objects of Exhibit
     *
     * @return list of Exhibit
     */
    @Override
    public List<Exhibit> findAll() {
        List<Exhibit> exhibitList = exhibitDao.loadForeignFields(exhibitDao.findAll());
        if (exhibitList.size() < 1) {
            throw new NotFoundException(ErrorMessage.OBJECT_NOT_FOUND);
        }
        return exhibitList;
    }

    /**
     * Method, that updates given object Exhibit
     *
     * @return true if the update was successful
     */
    @Override
    public boolean update(Exhibit exhibit) {
        if (exhibitDao.findByName(exhibit.getName()).isPresent()) {
            exhibitDao.update(exhibit);
            return true;
        } else {
            throw new NotUpdatedException(ErrorMessage.EXHIBIT_NOT_UPDATED);
        }
    }
    

    /**
     * Method for finding Exhibits by Author
     *
     * @param author Author of Exhibit
     * @return list of Exhibits that have given Author
     */
    @Override
    public List<Exhibit> findByAuthor(Author author) {
        List<Exhibit> exhibitList = exhibitDao.loadForeignFields(exhibitDao.findByAuthor(author));
        if (exhibitList.size() < 1) {
            throw new NotFoundException(ErrorMessage.OBJECT_NOT_FOUND);
        }
        return exhibitList;
    }

    /**
     * Method for finding Exhibits by Author, that linked with Audience where Exhibit is
     *
     * @param employee Employee for which you want find Exhibits
     * @return list of Exhibits
     */
    @Override
    public List<Exhibit> findByEmployee(Employee employee) {
        List<Exhibit> exhibitList = exhibitDao
                .loadForeignFields(exhibitDao.findByEmployee(employee));
        if (exhibitList.size() < 1) {
            
            throw new NotFoundException(ErrorMessage.OBJECT_NOT_FOUND);
        }
        return exhibitList;
    }

    /**
     * Method for updating Audience where Exhibit located
     *
     * @param exhibit Exhibit for which you want to update Audience
     * @param audience new Audience for given Exhibit
     * @return true if the update was successful
     */
    @Override
    public boolean updateExhibitAudience(Exhibit exhibit, Audience audience) {
        if ((exhibitDao.findById(exhibit.getId()).isPresent()) &&
                (audienceDao.findById(audience.getId()).isPresent())) {
            exhibitDao.updateAudience(exhibit, audience);
            return true;
        } else {
            throw new NotUpdatedException(ErrorMessage.EXHIBIT_AUDIENCE_NOT_UPDATED);
        }
    }
    
    @PostMapping
  public void post(@RequestBody Student student) {
      studentService.save(student);
    }


}

