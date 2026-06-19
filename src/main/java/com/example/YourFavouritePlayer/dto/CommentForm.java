package com.example.YourFavouritePlayer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentForm {

	@NotBlank(message = "Comment cannot be empty")
	@Size(max = 600, message = "Comment must be 600 characters or fewer")
	private String content;
}
