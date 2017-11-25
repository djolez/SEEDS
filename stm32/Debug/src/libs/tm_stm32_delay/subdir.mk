################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../src/libs/tm_stm32_delay/tm_stm32_delay.c 

OBJS += \
./src/libs/tm_stm32_delay/tm_stm32_delay.o 

C_DEPS += \
./src/libs/tm_stm32_delay/tm_stm32_delay.d 


# Each subdirectory must supply rules for building sources it contributes
src/libs/tm_stm32_delay/%.o: ../src/libs/tm_stm32_delay/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: MCU GCC Compiler'
	@echo %cd%
	arm-none-eabi-gcc -mcpu=cortex-m4 -mthumb -mfloat-abi=hard -mfpu=fpv4-sp-d16 -DSTM32L4 -DSTM32L476RGTx -DNUCLEO_L476RG -DSTM32 -DDEBUG -DUSE_HAL_DRIVER -DSTM32L476xx -DUSE_BAND_868 -DUSE_MODEM_LORA -DUSE_STM32L4XX_NUCLEO -DREGION_EU868 -I"C:/Users/Djordje/workspace/thesis/stm32/inc" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Conf" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Core" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Crypto" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Phy" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Mac" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Utilities" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/STM32L4xx_Nucleo" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/BSP/sx1272" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/BSP/sx1272mb2das" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/CMSIS/core" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/CMSIS/device" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/HAL_Driver/Inc/Legacy" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/HAL_Driver/Inc" -I"C:/Users/Djordje/workspace/thesis/stm32/src/common" -I"C:/Users/Djordje/workspace/thesis/stm32/src/tm_stm32_delay" -I"C:/Users/Djordje/workspace/thesis/stm32/src/devices/ds18b20/tm_stm32f4_ds18b20" -I"C:/Users/Djordje/workspace/thesis/stm32/src/devices/ds18b20/tm_stm32f4_onewire" -I"C:/Users/Djordje/workspace/thesis/stm32/src/devices/ds18b20" -I"C:/Users/Djordje/workspace/thesis/stm32/src/devices/dht11" -I"C:/Users/Djordje/workspace/thesis/stm32/src/devices/switch" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Mac/region" -O0 -g3 -Wall -fmessage-length=0 -ffunction-sections -c -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


