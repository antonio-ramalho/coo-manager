package com.manager.coomanager.userModule.enums;

import lombok.Getter;

@Getter
public enum Permission {

    COOPERATIVE_READ("Visualização dos dados da Cooperativa", "cooperative") {
        @Override
        public boolean canRead() {
            return true;
        }

        @Override
        public boolean canWrite() {
            return false;
        }
    },
    COOPERATIVE_WRITE("Edição dos dados da Cooperativa", "cooperative") {
        @Override
        public boolean canRead() {
            return true;
        }

        @Override
        public boolean canWrite() {
            return true;
        }
    },
    CLIENT_READ("Visualização de Clientes", "clients") {
        @Override
        public boolean canRead() {
            return true;
        }

        @Override
        public boolean canWrite() {
            return false;
        }
    },
    CLIENT_WRITE("Cadastro e Edição de Clientes", "clients") {
        @Override
        public boolean canRead() {
            return true;
        }

        @Override
        public boolean canWrite() {
            return true;
        }
    },
    FARMER_READ("Visualização de Produtores Rurais", "farmers") {
        @Override
        public boolean canRead() {
            return true;
        }

        @Override
        public boolean canWrite() {
            return false;
        }
    },
    FARMER_WRITE("Cadastro e Edição de Produtores Rurais", "farmers") {
        @Override
        public boolean canRead() {
            return true;
        }

        @Override
        public boolean canWrite() {
            return true;
        }
    },
    CONTRACT_READ("Visualização de Editais e Contratos", "contracts") {
        @Override
        public boolean canRead() {
            return true;
        }

        @Override
        public boolean canWrite() {
            return false;
        }
    },
    CONTRACT_WRITE("Gerenciamento de Editais e Contratos", "contracts") {
        @Override
        public boolean canRead() {
            return true;
        }

        @Override
        public boolean canWrite() {
            return true;
        }
    },
    INPUT_READ("Visualização de Insumos e Estoque", "inputs") {
        @Override
        public boolean canRead() {
            return true;
        }

        @Override
        public boolean canWrite() {
            return false;
        }
    },
    INPUT_WRITE("Gerenciamento de Insumos e Compras", "inputs") {
        @Override
        public boolean canRead() {
            return true;
        }

        @Override
        public boolean canWrite() {
            return true;
        }
    },
    SALES_READ("Visualização de Vendas e Logística", "sales") {
        @Override
        public boolean canRead() {
            return true;
        }

        @Override
        public boolean canWrite() {
            return false;
        }
    },
    SALES_WRITE("Emissão de Vendas e Termos Logísticos", "sales") {
        @Override
        public boolean canRead() {
            return true;
        }

        @Override
        public boolean canWrite() {
            return true;
        }
    },
    USER_MANAGE("Administração de Funcionários e Cargos", "users") {
        @Override
        public boolean canRead() {
            return true;
        }

        @Override
        public boolean canWrite() {
            return true;
        }

        @Override
        public boolean isAdministrative() {
            return true;
        }
    };

    private final String description;
    private final String module;

    Permission(String description, String module) {
        this.description = description;
        this.module = module;
    }

    public abstract boolean canRead();

    public abstract boolean canWrite();

    public boolean isAdministrative() {
        return false;
    }
}
