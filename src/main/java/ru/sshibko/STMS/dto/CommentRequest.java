package ru.sshibko.STMS.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequest {

    private String text;
}
