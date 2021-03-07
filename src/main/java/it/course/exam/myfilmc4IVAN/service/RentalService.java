package it.course.exam.myfilmc4IVAN.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class RentalService {

	
	public Date adjustDate(Date date) {
		LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

		Date correctDate = Date.from(localDateTime.atZone(ZoneOffset.UTC).toInstant());

		return correctDate;
	}



}
