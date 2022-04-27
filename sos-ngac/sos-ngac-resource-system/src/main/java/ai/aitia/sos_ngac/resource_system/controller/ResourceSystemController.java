package ai.aitia.sos_ngac.resource_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import ai.aitia.sos_ngac.resource_system.ResourceSystemConstants;
import ai.aitia.sos_ngac.resource_system.pep.PolicyEnforcementPoint;
import ai.aitia.sos_ngac.resource_system.pep.PolicyResponseDTO;
import ai.aitia.sos_ngac.resource_system.pep.ResourceRequestDTO;
import ai.aitia.sos_ngac.resource_system.rap.ResourceAccessPoint;

/* 
 * Controller class for mapping resource system provider services
 */

@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ResourceSystemController {

	// Components
	@Autowired 
	private PolicyEnforcementPoint pep;
	
	@Autowired
	private ResourceAccessPoint rap;
	
	
	// Mapping function for resource request service
	@PostMapping(value = ResourceSystemConstants.REQUEST_RESOURCE_URI)
	@ResponseBody
	public PolicyResponseDTO requestResource(@RequestBody final ResourceRequestDTO dto) throws Exception {
		rap.access(dto);
		return pep.queryPolicyServer(dto);
	} 
}
