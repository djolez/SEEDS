################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../src/debug.c \
../src/hw_gpio.c \
../src/hw_rtc.c \
../src/hw_spi.c \
../src/main.c \
../src/stm32l4xx_hal_msp.c \
../src/stm32l4xx_hw.c \
../src/stm32l4xx_it.c \
../src/syscalls.c \
../src/system_stm32l4xx.c \
../src/tim.c \
../src/vcom.c 

OBJS += \
./src/debug.o \
./src/hw_gpio.o \
./src/hw_rtc.o \
./src/hw_spi.o \
./src/main.o \
./src/stm32l4xx_hal_msp.o \
./src/stm32l4xx_hw.o \
./src/stm32l4xx_it.o \
./src/syscalls.o \
./src/system_stm32l4xx.o \
./src/tim.o \
./src/vcom.o 

C_DEPS += \
./src/debug.d \
./src/hw_gpio.d \
./src/hw_rtc.d \
./src/hw_spi.d \
./src/main.d \
./src/stm32l4xx_hal_msp.d \
./src/stm32l4xx_hw.d \
./src/stm32l4xx_it.d \
./src/syscalls.d \
./src/system_stm32l4xx.d \
./src/tim.d \
./src/vcom.d 


# Each subdirectory must supply rules for building sources it contributes
src/%.o: ../src/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: MCU GCC Compiler'
	@echo %cd%
	arm-none-eabi-gcc -mcpu=cortex-m4 -mthumb -mfloat-abi=hard -mfpu=fpv4-sp-d16 -DSTM32L4 -DSTM32L476RGTx -DNUCLEO_L476RG -DSTM32 -DDEBUG -DUSE_HAL_DRIVER -DSTM32L476xx -DUSE_BAND_868 -DUSE_MODEM_LORA -DUSE_STM32L4XX_NUCLEO -DREGION_EU868 -I"C:/Users/Djordje/workspace/thesis/stm32/inc" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Conf" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Core" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Crypto" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Phy" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Mac" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Utilities" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/STM32L4xx_Nucleo" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/BSP/sx1272" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/BSP/sx1272mb2das" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/CMSIS/core" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/CMSIS/device" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/HAL_Driver/Inc/Legacy" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/HAL_Driver/Inc" -I"C:/Users/Djordje/workspace/thesis/stm32/src/common" -I"C:/Users/Djordje/workspace/thesis/stm32/src/tm_stm32_delay" -I"C:/Users/Djordje/workspace/thesis/stm32/src/devices/ds18b20/tm_stm32f4_ds18b20" -I"C:/Users/Djordje/workspace/thesis/stm32/src/devices/ds18b20/tm_stm32f4_onewire" -I"C:/Users/Djordje/workspace/thesis/stm32/src/devices/ds18b20" -I"C:/Users/Djordje/workspace/thesis/stm32/src/devices/dht11" -I"C:/Users/Djordje/workspace/thesis/stm32/src/devices/switch" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Mac/region" -O0 -g3 -Wall -fmessage-length=0 -ffunction-sections -c -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


