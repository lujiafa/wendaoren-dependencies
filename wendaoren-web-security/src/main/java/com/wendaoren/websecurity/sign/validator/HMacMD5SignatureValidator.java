package com.wendaoren.websecurity.sign.validator;

import com.wendaoren.utils.common.DefaultValueUtils;
import com.wendaoren.websecurity.annotation.CheckSign;
import com.wendaoren.websecurity.exception.SignatureException;
import com.wendaoren.websecurity.exception.table.WebSecurityErrorCodeTable;
import com.wendaoren.websecurity.sign.AbstractSignatureValidator;
import com.wendaoren.websecurity.util.SignUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * HMacMD5签名验证器
 */
public class HMacMD5SignatureValidator extends AbstractSignatureValidator {
	
	private final static Logger logger = LoggerFactory.getLogger(HMacMD5SignatureValidator.class);

	@Override
	protected void doVerify(HttpServletRequest request, Method method, CheckSign checkSign, Map<String, String> signParamMap, String signKey, String sign) throws SignatureException {
		if (!SignUtils.verifyHMacMD5(signParamMap, DefaultValueUtils.defaultEmpty(signKey), sign)) {
			throw new SignatureException(WebSecurityErrorCodeTable.PARAM_SIGNATURE_VALID_FAIL.toErrorCode());
		}
	}
}