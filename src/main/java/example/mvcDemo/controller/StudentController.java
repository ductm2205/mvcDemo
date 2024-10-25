package example.mvcDemo.controller;

import example.mvcDemo.model.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final List<Student> students = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students", students);
        return "students/list";
    }

    @GetMapping("/{id}")
    public String viewStudent(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Student student = findStudentById(id);
        if (student == null) {
            redirectAttributes.addFlashAttribute("error", "Student not found");
            return "redirect:/students";
        }
        model.addAttribute("student", student);
        return "students/view";
    }

    @GetMapping("/new")
    public String createStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "students/create";
    }

    @PostMapping
    public String createStudent(@ModelAttribute Student student) {
        student.setId(idGenerator.incrementAndGet());
        students.add(student);
        return "redirect:/students";
    }

    @GetMapping("/{id}/edit")
    public String editStudentForm(@PathVariable Long id, Model model) {
        Student student = students.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
        model.addAttribute("student", student);
        return "students/edit";
    }

    @PostMapping("/{id}")
    public String updateStudent(@PathVariable Long id, @ModelAttribute Student student) {
        student.setId(id);
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(id)) {
                students.set(i, student);
                break;
            }
        }
        return "redirect:/students";
    }

    @GetMapping("/{id}/delete")
    public String deleteStudent(@PathVariable Long id) {
        students.removeIf(student -> student.getId().equals(id));
        return "redirect:/students";
    }

    /*
    HELPER FUNCTION
     */
    private Student findStudentById(Long id) {
        return students.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}

