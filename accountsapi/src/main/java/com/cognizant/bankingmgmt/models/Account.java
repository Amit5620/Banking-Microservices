package com.cognizant.bankingmgmt.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "accounts")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "account_number", nullable = false, length = 22)
	private String accountNumber;

	@Column(name = "customer_id", nullable = false)
	private Long customerId;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false, length = 50)
	private AccountType type;

	@Column(name = "balance", nullable = false, precision = 15, scale = 2)
	private BigDecimal balance;
	
	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private OffsetDateTime createdAt;

}
