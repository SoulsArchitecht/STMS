package ru.sshibko.STMS.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequest {

    @NotBlank(message = "Comment cannot be null")
    @Size(min = 1, max = 10000, message = "Max length of the text is 10000")
    private String text;
}
