package com.automationanywhere.botcommand.samples.commands.basic;

import com.automationanywhere.bot.service.GlobalSessionContext;
import com.automationanywhere.botcommand.BotCommand;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import java.lang.Boolean;
import java.lang.ClassCastException;
import java.lang.Deprecated;
import java.lang.Object;
import java.lang.String;
import java.lang.Throwable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Start_LoggingCommand implements BotCommand {
  private static final Logger logger = LogManager.getLogger(Start_LoggingCommand.class);

  private static final Messages MESSAGES_GENERIC = MessagesFactory.getMessages("com.automationanywhere.commandsdk.generic.messages");

  @Deprecated
  public Optional<Value> execute(Map<String, Value> parameters, Map<String, Object> sessionMap) {
    return execute(null, parameters, sessionMap);
  }

  public Optional<Value> execute(GlobalSessionContext globalSessionContext,
      Map<String, Value> parameters, Map<String, Object> sessionMap) {
    logger.traceEntry(() -> parameters != null ? parameters.entrySet().stream().filter(en -> !Arrays.asList( new String[] {}).contains(en.getKey()) && en.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).toString() : null, ()-> sessionMap != null ?sessionMap.toString() : null);
    Start_Logging command = new Start_Logging();
    HashMap<String, Object> convertedParameters = new HashMap<String, Object>();
    if(parameters.containsKey("dir") && parameters.get("dir") != null && parameters.get("dir").get() != null) {
      convertedParameters.put("dir", parameters.get("dir").get());
      if(convertedParameters.get("dir") !=null && !(convertedParameters.get("dir") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","dir", "String", parameters.get("dir").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("dir") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","dir"));
    }

    if(parameters.containsKey("forceDir") && parameters.get("forceDir") != null && parameters.get("forceDir").get() != null) {
      convertedParameters.put("forceDir", parameters.get("forceDir").get());
      if(convertedParameters.get("forceDir") !=null && !(convertedParameters.get("forceDir") instanceof Boolean)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","forceDir", "Boolean", parameters.get("forceDir").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("forceDir") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","forceDir"));
    }

    if(parameters.containsKey("bot_name") && parameters.get("bot_name") != null && parameters.get("bot_name").get() != null) {
      convertedParameters.put("bot_name", parameters.get("bot_name").get());
      if(convertedParameters.get("bot_name") !=null && !(convertedParameters.get("bot_name") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","bot_name", "String", parameters.get("bot_name").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("bot_name") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","bot_name"));
    }

    if(parameters.containsKey("envName") && parameters.get("envName") != null && parameters.get("envName").get() != null) {
      convertedParameters.put("envName", parameters.get("envName").get());
      if(convertedParameters.get("envName") !=null && !(convertedParameters.get("envName") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","envName", "String", parameters.get("envName").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("envName") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","envName"));
    }

    if(parameters.containsKey("log_type") && parameters.get("log_type") != null && parameters.get("log_type").get() != null) {
      convertedParameters.put("log_type", parameters.get("log_type").get());
      if(convertedParameters.get("log_type") !=null && !(convertedParameters.get("log_type") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","log_type", "String", parameters.get("log_type").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("log_type") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","log_type"));
    }
    if(convertedParameters.get("log_type") != null) {
      switch((String)convertedParameters.get("log_type")) {
        case "sf" : {

        } break;
        case "df" : {

        } break;
        case "ef" : {

        } break;
        default : throw new BotCommandException(MESSAGES_GENERIC.getString("generic.InvalidOption","log_type"));
      }
    }

    if(parameters.containsKey("mainTask") && parameters.get("mainTask") != null && parameters.get("mainTask").get() != null) {
      convertedParameters.put("mainTask", parameters.get("mainTask").get());
      if(convertedParameters.get("mainTask") !=null && !(convertedParameters.get("mainTask") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","mainTask", "String", parameters.get("mainTask").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("mainTask") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","mainTask"));
    }

    try {
      command.action((String)convertedParameters.get("dir"),(Boolean)convertedParameters.get("forceDir"),(String)convertedParameters.get("bot_name"),(String)convertedParameters.get("envName"),(String)convertedParameters.get("log_type"),(String)convertedParameters.get("mainTask"));Optional<Value> result = Optional.empty();
      return logger.traceExit(result);
    }
    catch (ClassCastException e) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.IllegalParameters","action"));
    }
    catch (BotCommandException e) {
      logger.fatal(e.getMessage(),e);
      throw e;
    }
    catch (Throwable e) {
      logger.fatal(e.getMessage(),e);
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.NotBotCommandException",e.getMessage()),e);
    }
  }
}
