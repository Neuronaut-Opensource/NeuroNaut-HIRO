package world.hiro.inventory.utilities;

import java.lang.StringBuilder;
import java.util.Random;
import java.util.List;
import java.security.SecureRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import world.hiro.inventory.model.Household;
import world.hiro.inventory.repository.HouseholdRepository;

@Component
public class InviteCodeGenerator {

    @Autowired HouseholdRepository householdRepository;

    public String createInviteCode() {
        boolean uniqueCode = false;
        String code = null;
        List<Household> listHousehold;
        while (!uniqueCode) {
            code = createRandomCode(6);
            listHousehold = householdRepository.findByInviteCode(code);
            if (listHousehold.size() == 0) {
                uniqueCode = true;
            }
        }
        return code;
    }

    public String createRandomCode(int codeLength){   
     char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new SecureRandom();
        for (int i = 0; i < codeLength; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }
}