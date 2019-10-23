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
    public Optional<BookInstance> findById(Long id) {
        String query = "SELECT * FROM book_instance where id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            return extractBookInstances(preparedStatement.executeQuery()).findAny();
        } catch (SQLException e) {
            log.error(e.getLocalizedMessage());
            throw new RuntimeException();
        }
    }

    private Stream<BookInstance> extractBookInstances(ResultSet resultSet) throws SQLException {
        Stream.Builder<BookInstance> builder = Stream.builder();
        while (resultSet.next()) {
            builder.add(
                    BookInstance.builder()
                            .id(resultSet.getLong("id"))
                            .isAvailable(resultSet.getBoolean("is_available"))
                            .build());
        }
        resultSet.close();
        return builder.build();
    }

    @Override
    public Book getInfoByBookInstance(Long bookInstanceId) {
        String query = "select books.title from book_instance left join books on "
                + "book_instance.id = books.id_book_instance where book_instance.id = ?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, bookInstanceId);
            ResultSet resultSet = preparedStatement.executeQuery();

            return Book.builder()
                    .title(resultSet.getString("title"))
                    .build();

        } catch (SQLException e) {
            log.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
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

