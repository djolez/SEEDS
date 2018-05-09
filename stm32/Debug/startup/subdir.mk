################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
S_SRCS += \
../startup/startup_stm32l476xx.s 

OBJS += \
./startup/startup_stm32l476xx.o 


# Each subdirectory must supply rules for building sources it contributes
startup/%.o: ../startup/%.s
	@echo 'Building file: $<'
	@echo 'Invoking: MCU GCC Assembler'
	@echo %cd%
	arm-none-eabi-as -mcpu=cortex-m4 -mthumb -mfloat-abi=hard -mfpu=fpv4-sp-d16 -I"C:/Users/Djordje/workspace/thesis/stm32/inc" -I"C:/Users/Djordje/workspace/thesis/stm32/CMSIS/core" -I"C:/Users/Djordje/workspace/thesis/stm32/CMSIS/device" -I"C:/Users/Djordje/workspace/thesis/stm32/HAL_Driver/Inc/Legacy" -I"C:/Users/Djordje/workspace/thesis/stm32/HAL_Driver/Inc" -I"C:/Users/Djordje/workspace/thesis/stm32/Utilities/STM32L4xx_Nucleo" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/BSP/sx1276" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/BSP/sx1276mb1las" -g -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


