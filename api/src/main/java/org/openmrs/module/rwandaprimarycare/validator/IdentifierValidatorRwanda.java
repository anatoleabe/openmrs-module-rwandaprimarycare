/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.rwandaprimarycare.validator;

import org.openmrs.module.idgen.validator.LuhnModNIdentifierValidator;
import org.openmrs.module.rwandaprimarycare.PrimaryCareUtil;
import org.openmrs.patient.IdentifierValidator;
import org.openmrs.patient.UnallowedIdentifierException;

/**
 * The Verhoeff Check Digit Validator catches all single errors and all adjacent transpositions.
 * See: http://www.cs.utsa.edu/~wagner/laws/verhoeff.html and Wagner, Neal.
 * "Verhoeff's Decimal Error Detection". The Laws of Cryptography with Java Code. p 54. San Antonio,
 * TX: 2003. http://www.cs.utsa.edu/~wagner/lawsbookcolor/laws.pdf
 */
public class IdentifierValidatorRwanda extends LuhnModNIdentifierValidator {
    
  
    /**
     * @see LuhnModNIdentifierValidator#getBaseCharacters()
     */
    @Override
    public String getBaseCharacters() {
        return "0123456789ACEFHJKMNPUWXY";
    }
    
    @Override
    public String getName() {
        return "Rwanda Luhn Mod-" + getBaseCharacters().length() + " Check-Digit Validator";
    }
    
    /** 
     * @see IdentifierValidator#getValidIdentifier(String)
     */
    @Override
    public String getValidIdentifier(String undecoratedIdentifier) throws UnallowedIdentifierException {
        String standardized = standardizeValidIdentifier(undecoratedIdentifier);
        String locationCode = PrimaryCareUtil.getPrimaryCareLocationCode();
        if (locationCode != null && !locationCode.equals("") ){
            standardized = locationCode + standardized;
        }
        return  standardized + "-" + computeCheckDigit(standardized);
    }
    
    
    /** 
     * @see IdentifierValidator#isValid(String)
     */
    @Override
    public boolean isValid(String identifier) throws UnallowedIdentifierException {
        try {
            identifier = standardizeValidIdentifier(identifier);
          //we can't use the getValidIdentifier method here because the location may be different, resulting in a different prefix.
          //so the comparison for old identifiers, during savePatient will fail, so:
            
            //remove the check-digit
            String semiUndecoratedIdentifier = identifier.substring(0, identifier.length()-2); 
            
            //re-calculate the check-digit
            String idTmp = semiUndecoratedIdentifier + "-" + computeCheckDigit(semiUndecoratedIdentifier);

            
            //TODO:   this could be integrated with the list of all possible MOH sites -- we need a location hierarchy widget.
            
            
            
            return identifier.equals(idTmp);
        }
        catch (Exception e) {
            throw new UnallowedIdentifierException("Invalid identifier specified for validator", e);
        }
    }
    
    
    
}
