################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../Drivers/BSP/sx1272/sx1272.c 

OBJS += \
./Drivers/BSP/sx1272/sx1272.o 

C_DEPS += \
./Drivers/BSP/sx1272/sx1272.d 


# Each subdirectory must supply rules for building sources it contributes
Drivers/BSP/sx1272/%.o: ../Drivers/BSP/sx1272/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: MCU GCC Compiler'
	@echo %cd%
	arm-none-eabi-gcc -mcpu=cortex-m4 -mthumb -mfloat-abi=hard -mfpu=fpv4-sp-d16 -DSTM32L4 -DSTM32L476RGTx -DNUCLEO_L476RG -DSTM32 -DDEBUG -DUSE_HAL_DRIVER -DSTM32L476xx -DUSE_BAND_868 -DUSE_MODEM_LORA -DUSE_STM32L4XX_NUCLEO -DREGION_EU868 -I"C:/Users/Djordje/workspace/lora_seeds/inc" -I"C:/Users/Djordje/workspace/lora_seeds/Middlewares/Lora/Conf" -I"C:/Users/Djordje/workspace/lora_seeds/Middlewares/Lora/Core" -I"C:/Users/Djordje/workspace/lora_seeds/Middlewares/Lora/Crypto" -I"C:/Users/Djordje/workspace/lora_seeds/Middlewares/Lora/Phy" -I"C:/Users/Djordje/workspace/lora_seeds/Middlewares/Lora/Mac" -I"C:/Users/Djordje/workspace/lora_seeds/Middlewares/Lora/Utilities" -I"C:/Users/Djordje/workspace/lora_seeds/Drivers/STM32L4xx_Nucleo" -I"C:/Users/Djordje/workspace/lora_seeds/Drivers/BSP/sx1272" -I"C:/Users/Djordje/workspace/lora_seeds/Drivers/BSP/sx1272mb2das" -I"C:/Users/Djordje/workspace/lora_seeds/Drivers/CMSIS/core" -I"C:/Users/Djordje/workspace/lora_seeds/Drivers/CMSIS/device" -I"C:/Users/Djordje/workspace/lora_seeds/Drivers/HAL_Driver/Inc/Legacy" -I"C:/Users/Djordje/workspace/lora_seeds/Drivers/HAL_Driver/Inc" -I"C:/Users/Djordje/workspace/lora_seeds/src/common" -I"C:/Users/Djordje/workspace/lora_seeds/src/tm_stm32_delay" -I"C:/Users/Djordje/workspace/lora_seeds/src/devices/ds18b20/tm_stm32f4_ds18b20" -I"C:/Users/Djordje/workspace/lora_seeds/src/devices/ds18b20/tm_stm32f4_onewire" -I"C:/Users/Djordje/workspace/lora_seeds/src/devices/ds18b20" -I"C:/Users/Djordje/workspace/lora_seeds/src/devices/dht11" -I"C:/Users/Djordje/workspace/lora_seeds/src/devices/switch" -I"C:/Users/Djordje/workspace/lora_seeds/Middlewares/Lora/Mac/region" -O0 -g3 -Wall -fmessage-length=0 -ffunction-sections -c -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


