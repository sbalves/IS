package dto;

import lombok.Data;

@Data
public class CreateStudentWebRequest {

  private String name;
  private String birth_date;
  private Integer completed__credits;
  private Integer average_grade;
}
