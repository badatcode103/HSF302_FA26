package com.example.data.service;
import com.example.data.exception.StudentNotFoundException;
import com.example.data.exception.ValidationException;
import com.example.data.pojo.Student;
import com.example.data.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
@Service @Transactional(readOnly=true)
public class StudentServiceImpl implements StudentService {
 private static final Pattern EMAIL=Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[\\w.-]+$");
 private final StudentRepository repository;
 public StudentServiceImpl(StudentRepository repository){this.repository=repository;}
 @Override @Transactional public Student create(Student s){normalize(s); validate(s); if(repository.existsByEmailIgnoreCase(s.getEmail())) throw new ValidationException("Email already exists"); return repository.save(s);}
 @Override public Student findById(Long id){return repository.findById(id).orElseThrow(()->new StudentNotFoundException(id));}
 @Override public List<Student> findAll(){return repository.findAllByOrderByIdAsc();}
 @Override @Transactional public Student update(Student s){Student existing=findById(s.getId()); normalize(s); validate(s); if(repository.existsByEmailIgnoreCaseAndIdNot(s.getEmail(),s.getId())) throw new ValidationException("Email already exists"); existing.setName(s.getName()); existing.setEmail(s.getEmail()); existing.setAge(s.getAge()); return repository.save(existing);}
 @Override @Transactional public void delete(Long id){if(!repository.existsById(id)) throw new StudentNotFoundException(id); repository.deleteById(id);}
 @Override public List<Student> search(String keyword){return keyword==null || keyword.isBlank()?findAll():repository.search(keyword.trim());}
 private void normalize(Student s){if(s.getName()!=null)s.setName(s.getName().trim()); if(s.getEmail()!=null)s.setEmail(s.getEmail().trim().toLowerCase(Locale.ROOT));}
 private void validate(Student s){if(s.getName()==null || s.getName().isBlank())throw new ValidationException("Name is required"); if(s.getName().length()>100)throw new ValidationException("Name must be at most 100 characters"); if(s.getEmail()==null || !EMAIL.matcher(s.getEmail()).matches())throw new ValidationException("Invalid email"); if(s.getAge()==null || s.getAge()<18 || s.getAge()>60)throw new ValidationException("Age must be between 18 and 60");}
}
