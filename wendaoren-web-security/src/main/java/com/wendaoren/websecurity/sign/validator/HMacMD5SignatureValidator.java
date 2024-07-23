package com.wendaoren.websecurity.sign.validator;

import com.wendaoren.core.constant.ErrorCodeConstant;
import com.wendaoren.core.exception.ErrorCode;
import com.wendaoren.utils.common.DefaultValueUtils;
import com.wendaoren.websecurity.annotation.CheckSign;
import com.wendaoren.websecurity.exception.SignatureException;
import com.wendaoren.websecurity.sign.AbstractSignatureValidator;
import com.wendaoren.utils.crypto.SignUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * HMacMD5签名验证器
 */
public class HMacMD5SignatureValidator extends AbstractSignatureValidator {
	
	private final static Logger logger = LoggerFactory.getLogger(HMacMD5SignatureValidator.class);

	@Override
	protected void doVerify(HttpServletRequest request, Method method, CheckSign checkSign, Map<String, String> signParamMap, String signKey, String sign) throws SignatureException {
		try {
			if (!SignUtils.verifyHMacMD5(signParamMap, DefaultValueUtils.defaultEmpty(signKey), sign)) {
				throw new SignatureException(ErrorCode.build(ErrorCodeConstant.INVALID_SIGNATURE_INFO, request.getLocale()));
			}
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug("签名验证失败 - {}", e.getMessage(), e);
			}
			throw new SignatureException(ErrorCode.build(ErrorCodeConstant.INVALID_SIGNATURE_INFO, request.getLocale()));
		}
	}
}