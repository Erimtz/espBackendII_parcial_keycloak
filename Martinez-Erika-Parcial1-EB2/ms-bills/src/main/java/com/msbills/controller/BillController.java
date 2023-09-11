package com.msbills.controller;

import com.msbills.models.Bill;
import com.msbills.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService service;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_user')")
    public ResponseEntity<List<Bill>> getAll() {
        return ResponseEntity.ok().body(service.getAllBill());
    }

    @PostMapping("/group")
    @PreAuthorize("hasAnyAuthority('GROUP_account')")
    public ResponseEntity<String> createBill(@RequestBody Bill bill) {
        boolean created = service.createBill(bill);
        if (created) {
            return ResponseEntity.ok("Factura creada con éxito.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo crear la factura.");
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ROLE_user')")
    public ResponseEntity<List<Bill>> getBillsByUserId(@PathVariable String userId) {
        List<Bill> userBills = service.getBillsByUserId(userId);
        if (userBills != null && !userBills.isEmpty()) {
            return ResponseEntity.ok(userBills);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

}
