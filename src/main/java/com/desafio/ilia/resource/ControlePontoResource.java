package com.desafio.ilia.resource;

import com.desafio.ilia.service.AlocacoesService;
import com.desafio.ilia.service.RegistroService;
import com.desafio.ilia.service.RelatorioService;
import com.desafio.ilia.service.dto.RegistroDTO;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.desafio.ilia.service.dto.AlocacaoDTO;
import com.desafio.ilia.service.dto.RelatorioDTO;

import javax.validation.Valid;

@RestController
public class ControlePontoResource {
	
	@Autowired
	private RegistroService registroService;

	@Autowired
	private AlocacoesService alocacoesService;

	@Autowired
    private RelatorioService relatorioService;

	@PostMapping("/batidas")
    public ResponseEntity<Void> registroPonto(@Valid @RequestBody RegistroDTO registro) {
        registroService.registroPonto(registro);
        return ResponseEntity.created(null).build();
    }

	
	@PostMapping("/alocacoes")
    public ResponseEntity<Void> registroAlocacao(@Valid @RequestBody AlocacaoDTO alocacao) {
        alocacoesService.registroAlocacao(alocacao);
        return ResponseEntity.created(null).build();
    }

	  
    @GetMapping("/folhas-de-ponto/{mes}")
    public ResponseEntity<RelatorioDTO> getFolhasPonto(@PathVariable String mes) {
        RelatorioDTO relatorioDTO = relatorioService.getFolhasPonto(mes);
	    return ResponseEntity.ok().body(relatorioDTO);
    }
}
