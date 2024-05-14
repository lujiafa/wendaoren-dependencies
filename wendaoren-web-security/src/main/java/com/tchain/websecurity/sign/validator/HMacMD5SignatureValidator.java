package com.tchain.websecurity.sign.validator;

import com.tchain.utils.common.DefaultValueUtils;
import com.tchain.websecurity.annotation.CheckSign;
import com.tchain.websecurity.exception.SignatureException;
import com.tchain.websecurity.exception.table.WebSecurityErrorCodeTable;
import com.tchain.websecurity.sign.AbstractSignatureValidator;
import com.tchain.websecurity.util.SignUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * HMacMD5签名验证器
 */
public class HMacMD5SignatureValidator extends AbstractSignatureValidator {
	
	private final static Logger logger = LoggerFactory.getLogger(HMacMD5SignatureValidator.class);

	@Override
	protected void doVerify(HttpServletRequest request, Method method, CheckSign checkSign, Map<String, String> signParamMap, String signKey, String sign) throws SignatureException {
		String newSign = SignUtils.signHMacMD5(signParamMap, DefaultValueUtils.defaultEmpty(signKey));
		if (!sign.equalsIgnoreCase(newSign)) {
			throw new SignatureException(WebSecurityErrorCodeTable.PARAM_SIGNATURE_VALID_FAIL.toErrorCode());
		}
	}
}