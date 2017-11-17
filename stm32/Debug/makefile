################################################################################
# Automatically-generated file. Do not edit!
################################################################################

-include ../makefile.init

RM := rm -rf

# All of the sources participating in the build are defined here
-include sources.mk
-include startup/subdir.mk
-include src/libs/tm_stm32_delay/subdir.mk
-include src/devices/switch/subdir.mk
-include src/devices/ds18b20/tm_stm32f4_onewire/subdir.mk
-include src/devices/ds18b20/tm_stm32f4_ds18b20/subdir.mk
-include src/devices/ds18b20/subdir.mk
-include src/devices/dht11/subdir.mk
-include src/common/subdir.mk
-include src/subdir.mk
-include Middlewares/Lora/Utilities/subdir.mk
-include Middlewares/Lora/Mac/region/subdir.mk
-include Middlewares/Lora/Mac/subdir.mk
-include Middlewares/Lora/Crypto/subdir.mk
-include Middlewares/Lora/Core/subdir.mk
-include Middlewares/Lora/Conf/Src/subdir.mk
-include Drivers/STM32L4xx_Nucleo/subdir.mk
-include Drivers/HAL_Driver/Src/subdir.mk
-include Drivers/HAL_Driver/Inc/subdir.mk
-include Drivers/BSP/sx1272mb2das/subdir.mk
-include Drivers/BSP/sx1272/subdir.mk
-include subdir.mk
-include objects.mk

ifneq ($(MAKECMDGOALS),clean)
ifneq ($(strip $(S_UPPER_DEPS)),)
-include $(S_UPPER_DEPS)
endif
ifneq ($(strip $(C_DEPS)),)
-include $(C_DEPS)
endif
endif

-include ../makefile.defs

# Add inputs and outputs from these tool invocations to the build variables 

# All Target
all: lora_seeds.elf

# Tool invocations
lora_seeds.elf: $(OBJS) $(USER_OBJS)
	@echo 'Building target: $@'
	@echo 'Invoking: MCU GCC Linker'
	arm-none-eabi-gcc -mcpu=cortex-m4 -mthumb -mfloat-abi=hard -mfpu=fpv4-sp-d16 -T"C:\Users\Djordje\workspace\lora_seeds\LinkerScript.ld" -Wl,-Map=output.map -Wl,--gc-sections -lm -o "lora_seeds.elf" @"objects.list" $(USER_OBJS) $(LIBS)
	@echo 'Finished building target: $@'
	@echo ' '
	$(MAKE) --no-print-directory post-build

# Other Targets
clean:
	-$(RM) *
	-@echo ' '

post-build:
	-@echo 'Generating binary and Printing size information:'
	arm-none-eabi-objcopy -O binary "lora_seeds.elf" "lora_seeds.bin"
	arm-none-eabi-size "lora_seeds.elf"
	-@echo ' '

.PHONY: all clean dependents
.SECONDARY: post-build

-include ../makefile.targets
