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

public final class AddLogCommand implements BotCommand {
  private static final Logger logger = LogManager.getLogger(AddLogCommand.class);

  private static final Messages MESSAGES_GENERIC = MessagesFactory.getMessages("com.automationanywhere.commandsdk.generic.messages");

  @Deprecated
  public Optional<Value> execute(Map<String, Value> parameters, Map<String, Object> sessionMap) {
    return execute(null, parameters, sessionMap);
  }

  public Optional<Value> execute(GlobalSessionContext globalSessionContext,
      Map<String, Value> parameters, Map<String, Object> sessionMap) {
    logger.traceEntry(() -> parameters != null ? parameters.entrySet().stream().filter(en -> !Arrays.asList( new String[] {}).contains(en.getKey()) && en.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).toString() : null, ()-> sessionMap != null ?sessionMap.toString() : null);
    AddLog command = new AddLog();
    HashMap<String, Object> convertedParameters = new HashMap<String, Object>();
    if(parameters.containsKey("curr_task") && parameters.get("curr_task") != null && parameters.get("curr_task").get() != null) {
      convertedParameters.put("curr_task", parameters.get("curr_task").get());
      if(convertedParameters.get("curr_task") !=null && !(convertedParameters.get("curr_task") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","curr_task", "String", parameters.get("curr_task").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("curr_task") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","curr_task"));
    }

    if(parameters.containsKey("status") && parameters.get("status") != null && parameters.get("status").get() != null) {
      convertedParameters.put("status", parameters.get("status").get());
      if(convertedParameters.get("status") !=null && !(convertedParameters.get("status") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","status", "String", parameters.get("status").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("status") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","status"));
    }
    if(convertedParameters.get("status") != null) {
      switch((String)convertedParameters.get("status")) {
        case "SUCCESS" : {

        } break;
        case "INFO" : {

        } break;
        case "WARNING" : {

        } break;
        case "FAILURE" : {

        } break;
        case "CUSTOM" : {
          if(parameters.containsKey("custom_status") && parameters.get("custom_status") != null && parameters.get("custom_status").get() != null) {
            convertedParameters.put("custom_status", parameters.get("custom_status").get());
            if(convertedParameters.get("custom_status") !=null && !(convertedParameters.get("custom_status") instanceof String)) {
              throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","custom_status", "String", parameters.get("custom_status").get().getClass().getSimpleName()));
            }
          }
          if(convertedParameters.get("custom_status") == null) {
            throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","custom_status"));
          }


        } break;
        default : throw new BotCommandException(MESSAGES_GENERIC.getString("generic.InvalidOption","status"));
      }
    }

    if(parameters.containsKey("message") && parameters.get("message") != null && parameters.get("message").get() != null) {
      convertedParameters.put("message", parameters.get("message").get());
      if(convertedParameters.get("message") !=null && !(convertedParameters.get("message") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","message", "String", parameters.get("message").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("message") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","message"));
    }

    if(parameters.containsKey("disableLogs") && parameters.get("disableLogs") != null && parameters.get("disableLogs").get() != null) {
      convertedParameters.put("disableLogs", parameters.get("disableLogs").get());
      if(convertedParameters.get("disableLogs") !=null && !(convertedParameters.get("disableLogs") instanceof Boolean)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","disableLogs", "Boolean", parameters.get("disableLogs").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("disableLogs") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","disableLogs"));
    }

    try {
      command.action((String)convertedParameters.get("curr_task"),(String)convertedParameters.get("status"),(String)convertedParameters.get("custom_status"),(String)convertedParameters.get("message"),(Boolean)convertedParameters.get("disableLogs"));Optional<Value> result = Optional.empty();
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
