-- ========================================
-- SCRIPT DE DADOS MOCKADOS - MEDHEALTH
-- ========================================
-- Este script insere dados de teste para demonstração
-- Volume: 50 pacientes, 20 médicos, 100+ consultas
-- Execute(Linux): docker exec -i medhealth-db psql -U meduser -d medhealth < mock_data.sql
-- Execute(PowerShell): Get-Content mock_data.sql | docker exec -i medhealth-db psql -U meduser -d medhealth
-- ========================================

\echo '🚀 Iniciando inserção de dados mockados...'
\echo ''

-- ========================================
-- 1. ENDEREÇOS (ADDRESSES)
-- ========================================
\echo '📍 Inserindo endereços...'

-- Endereços para médicos (IDs 2-21)
INSERT INTO addresses (state, city, neighborhood, street, number, zip_code, complement) VALUES
('BA', 'Salvador', 'Barra', 'Av. Oceânica', '100', '40140-130', 'Sala 201'),
('BA', 'Salvador', 'Pituba', 'Av. Paulo VI', '200', '41810-001', 'Ed. Medical Center'),
('BA', 'Salvador', 'Ondina', 'Av. Oceanica', '300', '40170-010', 'Consultório 5'),
('BA', 'Salvador', 'Caminho das Árvores', 'Av. Tancredo Neves', '400', '41820-021', 'Torre B - Sala 1202'),
('BA', 'Salvador', 'Itaigara', 'Rua Altino Serbeto', '500', '41825-010', NULL),
('BA', 'Salvador', 'Brotas', 'Rua Barão de Loreto', '600', '40285-001', 'Casa'),
('BA', 'Salvador', 'Graça', 'Rua Rodolfo Coelho', '700', '40150-130', 'Apt 302'),
('BA', 'Salvador', 'Rio Vermelho', 'Largo de Santana', '800', '41940-360', NULL),
('BA', 'Salvador', 'Vitória', 'Av. Sete de Setembro', '900', '40080-002', 'Clínica Saúde+'),
('BA', 'Salvador', 'Centro', 'Rua Chile', '1000', '40020-000', 'Ed. Empresarial'),
('BA', 'Salvador', 'Nazaré', 'Av. Joana Angélica', '1100', '40050-001', NULL),
('BA', 'Salvador', 'Federação', 'Rua Caetano Moura', '1200', '40210-340', 'Sala 15'),
('BA', 'Salvador', 'Canela', 'Ladeira da Barra', '1300', '40110-160', NULL),
('BA', 'Salvador', 'Campo Grande', 'Rua Carlos Gomes', '1400', '40301-350', 'Sobreloja'),
('BA', 'Salvador', 'Garcia', 'Av. Vasco da Gama', '1500', '40100-200', NULL),
('BA', 'Salvador', 'Amaralina', 'Rua Prof. Aristides Novis', '1600', '41900-350', 'Apt 501'),
('BA', 'Salvador', 'Pituba', 'Rua das Hortênsias', '1700', '41810-011', NULL),
('BA', 'Salvador', 'Barra', 'Rua Afonso Celso', '1800', '40140-080', 'Consultório 8'),
('BA', 'Salvador', 'Ondina', 'Rua Wanderley Pinho', '1900', '40170-130', NULL),
('BA', 'Salvador', 'Itaigara', 'Rua Goiás', '2000', '41825-903', 'Ed. Corporate');

-- Endereços para pacientes (IDs 22-71)
INSERT INTO addresses (state, city, neighborhood, street, number, zip_code, complement) VALUES
('BA', 'Salvador', 'Cabula', 'Rua Silveira Martins', '101', '41150-000', 'Apt 203'),
('BA', 'Salvador', 'Pernambués', 'Rua Direta do Saboeiro', '102', '41100-000', NULL),
('BA', 'Salvador', 'Liberdade', 'Rua Lima e Silva', '103', '40323-000', 'Casa'),
('BA', 'Salvador', 'São Caetano', 'Av. Afrânio Peixoto', '104', '40380-000', NULL),
('BA', 'Salvador', 'Barris', 'Rua Silveira Martins', '105', '40070-000', 'Apt 102'),
('BA', 'Salvador', 'Tororó', 'Rua do Tororó', '106', '40055-000', NULL),
('BA', 'Salvador', 'Matatu', 'Rua da Paz', '107', '40255-000', 'Casa B'),
('BA', 'Salvador', 'IAPI', 'Rua Marechal Floriano', '108', '40330-000', NULL),
('BA', 'Salvador', 'Cajazeiras', 'Rua da Coelba', '109', '41338-000', 'Apt 401'),
('BA', 'Salvador', 'Periperi', 'Rua da Alegria', '110', '40725-000', NULL),
('BA', 'Salvador', 'Paripe', 'Rua do Porto', '111', '40800-000', 'Casa 2'),
('BA', 'Salvador', 'Plataforma', 'Av. Suburbana', '112', '40715-000', NULL),
('BA', 'Salvador', 'Lobato', 'Rua da Felicidade', '113', '40420-000', 'Fundos'),
('BA', 'Salvador', 'Massaranduba', 'Rua São João', '114', '40421-000', NULL),
('BA', 'Salvador', 'Jardim Armação', 'Rua da Praia', '115', '41750-000', 'Casa 5'),
('BA', 'Salvador', 'Boca do Rio', 'Av. Jorge Amado', '116', '41706-000', NULL),
('BA', 'Salvador', 'Patamares', 'Alameda Praia de Atalaia', '117', '41680-000', 'Apt 1501'),
('BA', 'Salvador', 'Imbuí', 'Rua Edistio Pondé', '118', '41720-050', NULL),
('BA', 'Salvador', 'Piatã', 'Rua Praia de Piatã', '119', '41650-000', 'Casa'),
('BA', 'Salvador', 'Itapuã', 'Rua K', '120', '41620-000', NULL),
('BA', 'Salvador', 'Stella Maris', 'Alameda Praia de Guarajuba', '121', '41600-000', 'Apt 201'),
('BA', 'Salvador', 'Flamengo', 'Rua da Grécia', '122', '41603-000', NULL),
('BA', 'Salvador', 'Costa Azul', 'Av. Octávio Mangabeira', '123', '41760-000', 'Bloco A'),
('BA', 'Salvador', 'Jardim Apipema', 'Rua do Apipema', '124', '40155-210', NULL),
('BA', 'Salvador', 'Alto do Cabrito', 'Rua do Cabrito', '125', '40301-000', 'Casa 3'),
('BA', 'Salvador', 'Cosme de Farias', 'Rua do Cosme', '126', '40253-000', NULL),
('BA', 'Salvador', 'Engenho Velho', 'Ladeira do Engenho', '127', '40243-000', 'Apt 5'),
('BA', 'Salvador', 'Pau Miúdo', 'Rua do Pau Miúdo', '128', '40320-000', NULL),
('BA', 'Salvador', 'Uruguai', 'Rua Uruguai', '129', '40410-000', 'Casa'),
('BA', 'Salvador', 'Arenoso', 'Rua do Arenoso', '130', '40415-000', NULL),
('BA', 'Salvador', 'Pirajá', 'Estrada do Pirajá', '131', '41233-000', 'Lote 10'),
('BA', 'Salvador', 'São Cristóvão', 'Rua São Cristóvão', '132', '40226-000', NULL),
('BA', 'Salvador', 'Acupe de Brotas', 'Rua do Acupe', '133', '40285-500', 'Apt 101'),
('BA', 'Salvador', 'Lapinha', 'Rua da Lapinha', '134', '40421-000', NULL),
('BA', 'Salvador', 'Caixa D Água', 'Rua da Caixa', '135', '40365-000', 'Casa 7'),
('BA', 'Salvador', 'Alto das Pombas', 'Travessa das Pombas', '136', '40255-100', NULL),
('BA', 'Salvador', 'Sussuarana', 'Rua Nova', '137', '41213-000', 'Apt 302'),
('BA', 'Salvador', 'Mussurunga', 'Rua do Sossego', '138', '41500-000', NULL),
('BA', 'Salvador', 'Valéria', 'Estrada da Valéria', '139', '41310-000', 'Casa'),
('BA', 'Salvador', 'Castelo Branco', 'Rua do Castelo', '140', '41330-000', NULL),
('BA', 'Salvador', 'Vila Canária', 'Rua Canário', '141', '41350-000', 'Apt 205'),
('BA', 'Salvador', 'Sete de Abril', 'Rua Sete de Abril', '142', '41231-000', NULL),
('BA', 'Salvador', 'Fazenda Coutos', 'Estrada Velha', '143', '41237-000', 'Lote 5'),
('BA', 'Salvador', 'Saramandaia', 'Rua Saramandaia', '144', '41150-300', NULL),
('BA', 'Salvador', 'Doron', 'Rua do Doron', '145', '41235-000', 'Casa B'),
('BA', 'Salvador', 'Pituaçu', 'Av. Pituaçu', '146', '41740-000', NULL),
('BA', 'Salvador', 'Stiep', 'Rua do Stiep', '147', '41770-000', 'Cond. Residencial'),
('BA', 'Salvador', 'Horto Florestal', 'Rua Mello Moraes', '148', '40295-650', NULL),
('BA', 'Salvador', 'Candeal', 'Rua do Candeal', '149', '40296-000', 'Casa 12'),
('BA', 'Salvador', 'Santa Cruz', 'Rua Santa Cruz', '150', '40255-200', NULL);

\echo '   ✓ 50 endereços inseridos'
\echo ''

-- ========================================
-- 2. PESSOAS (PERSONS)
-- ========================================
\echo '👥 Inserindo pessoas...'

-- Pessoas para médicos (IDs 2-21)
INSERT INTO persons (full_name, email, phone, cpf, address_id, status) VALUES
('Dr. Carlos Eduardo Mendes', 'carlos.mendes@medhealth.com', '(71) 98111-0001', '111.111.111-11', 2, 'ACTIVE'),
('Dra. Maria Fernanda Costa', 'maria.costa@medhealth.com', '(71) 98111-0002', '222.222.222-22', 3, 'ACTIVE'),
('Dr. João Pedro Oliveira', 'joao.oliveira@medhealth.com', '(71) 98111-0003', '333.333.333-33', 4, 'ACTIVE'),
('Dra. Ana Carolina Silva', 'ana.silva@medhealth.com', '(71) 98111-0004', '444.444.444-44', 5, 'ACTIVE'),
('Dr. Roberto Santos', 'roberto.santos@medhealth.com', '(71) 98111-0005', '555.555.555-55', 6, 'ACTIVE'),
('Dra. Patricia Lima', 'patricia.lima@medhealth.com', '(71) 98111-0006', '666.666.666-66', 7, 'ACTIVE'),
('Dr. Fernando Alves', 'fernando.alves@medhealth.com', '(71) 98111-0007', '777.777.777-77', 8, 'ACTIVE'),
('Dra. Juliana Rocha', 'juliana.rocha@medhealth.com', '(71) 98111-0008', '888.888.888-88', 9, 'ACTIVE'),
('Dr. Ricardo Pereira', 'ricardo.pereira@medhealth.com', '(71) 98111-0009', '999.999.999-99', 10, 'ACTIVE'),
('Dra. Beatriz Martins', 'beatriz.martins@medhealth.com', '(71) 98111-0010', '101.010.101-01', 11, 'ACTIVE'),
('Dr. Lucas Ferreira', 'lucas.ferreira@medhealth.com', '(71) 98111-0011', '202.020.202-02', 12, 'ACTIVE'),
('Dra. Camila Souza', 'camila.souza@medhealth.com', '(71) 98111-0012', '303.030.303-03', 13, 'ACTIVE'),
('Dr. Rafael Barbosa', 'rafael.barbosa@medhealth.com', '(71) 98111-0013', '404.040.404-04', 14, 'ACTIVE'),
('Dra. Leticia Carvalho', 'leticia.carvalho@medhealth.com', '(71) 98111-0014', '505.050.505-05', 15, 'ACTIVE'),
('Dr. Gustavo Moreira', 'gustavo.moreira@medhealth.com', '(71) 98111-0015', '606.060.606-06', 16, 'ACTIVE'),
('Dra. Renata Dias', 'renata.dias@medhealth.com', '(71) 98111-0016', '707.070.707-07', 17, 'ACTIVE'),
('Dr. Bruno Ribeiro', 'bruno.ribeiro@medhealth.com', '(71) 98111-0017', '808.080.808-08', 18, 'ACTIVE'),
('Dra. Larissa Pinto', 'larissa.pinto@medhealth.com', '(71) 98111-0018', '909.090.909-09', 19, 'ACTIVE'),
('Dr. Thiago Azevedo', 'thiago.azevedo@medhealth.com', '(71) 98111-0019', '121.212.121-21', 20, 'ACTIVE'),
('Dra. Isabela Monteiro', 'isabela.monteiro@medhealth.com', '(71) 98111-0020', '131.313.131-31', 21, 'ACTIVE');

-- Pessoas para pacientes (IDs 22-71)
INSERT INTO persons (full_name, email, phone, cpf, address_id, status) VALUES
('Maria da Silva', 'maria.silva@email.com', '(71) 99000-0001', '141.414.141-41', 22, 'ACTIVE'),
('José dos Santos', 'jose.santos@email.com', '(71) 99000-0002', '151.515.151-51', 23, 'ACTIVE'),
('Ana Paula Costa', 'ana.costa@email.com', '(71) 99000-0003', '161.616.161-61', 24, 'ACTIVE'),
('Pedro Henrique Lima', 'pedro.lima@email.com', '(71) 99000-0004', '171.717.171-71', 25, 'ACTIVE'),
('Juliana Oliveira', 'juliana.oliveira@email.com', '(71) 99000-0005', '181.818.181-81', 26, 'ACTIVE'),
('Carlos Alberto Souza', 'carlos.souza@email.com', '(71) 99000-0006', '191.919.191-91', 27, 'ACTIVE'),
('Fernanda Rodrigues', 'fernanda.rodrigues@email.com', '(71) 99000-0007', '212.121.212-12', 28, 'ACTIVE'),
('Ricardo Alves', 'ricardo.alves@email.com', '(71) 99000-0008', '232.323.232-32', 29, 'ACTIVE'),
('Patricia Santos', 'patricia.santos@email.com', '(71) 99000-0009', '242.424.242-42', 30, 'ACTIVE'),
('Lucas Pereira', 'lucas.pereira@email.com', '(71) 99000-0010', '252.525.252-52', 31, 'ACTIVE'),
('Beatriz Martins', 'beatriz.martins.paciente@email.com', '(71) 99000-0011', '262.626.262-62', 32, 'ACTIVE'),
('Felipe Costa', 'felipe.costa@email.com', '(71) 99000-0012', '272.727.272-72', 33, 'ACTIVE'),
('Amanda Silva', 'amanda.silva@email.com', '(71) 99000-0013', '282.828.282-82', 34, 'ACTIVE'),
('Rodrigo Fernandes', 'rodrigo.fernandes@email.com', '(71) 99000-0014', '292.929.292-92', 35, 'ACTIVE'),
('Camila Barbosa', 'camila.barbosa@email.com', '(71) 99000-0015', '313.131.313-13', 36, 'ACTIVE'),
('Bruno Moreira', 'bruno.moreira@email.com', '(71) 99000-0016', '323.232.323-23', 37, 'ACTIVE'),
('Larissa Dias', 'larissa.dias@email.com', '(71) 99000-0017', '333.333.333-34', 38, 'ACTIVE'),
('Thiago Ribeiro', 'thiago.ribeiro@email.com', '(71) 99000-0018', '343.434.343-43', 39, 'ACTIVE'),
('Isabela Pinto', 'isabela.pinto@email.com', '(71) 99000-0019', '353.535.353-53', 40, 'ACTIVE'),
('Gabriel Azevedo', 'gabriel.azevedo@email.com', '(71) 99000-0020', '363.636.363-63', 41, 'ACTIVE'),
('Renata Monteiro', 'renata.monteiro@email.com', '(71) 99000-0021', '373.737.373-73', 42, 'ACTIVE'),
('Diego Santos', 'diego.santos@email.com', '(71) 99000-0022', '383.838.383-83', 43, 'ACTIVE'),
('Mariana Costa', 'mariana.costa@email.com', '(71) 99000-0023', '393.939.393-93', 44, 'ACTIVE'),
('Leonardo Silva', 'leonardo.silva@email.com', '(71) 99000-0024', '414.141.414-14', 45, 'ACTIVE'),
('Carla Oliveira', 'carla.oliveira@email.com', '(71) 99000-0025', '424.242.424-24', 46, 'ACTIVE'),
('Fábio Lima', 'fabio.lima@email.com', '(71) 99000-0026', '434.343.434-34', 47, 'ACTIVE'),
('Vanessa Rodrigues', 'vanessa.rodrigues@email.com', '(71) 99000-0027', '444.444.444-45', 48, 'ACTIVE'),
('Rafael Santos', 'rafael.santos.paciente@email.com', '(71) 99000-0028', '454.545.454-54', 49, 'ACTIVE'),
('Priscila Alves', 'priscila.alves@email.com', '(71) 99000-0029', '464.646.464-64', 50, 'ACTIVE'),
('Marcelo Pereira', 'marcelo.pereira@email.com', '(71) 99000-0030', '474.747.474-74', 51, 'ACTIVE'),
('Tatiane Martins', 'tatiane.martins@email.com', '(71) 99000-0031', '484.848.484-84', 52, 'ACTIVE'),
('Eduardo Costa', 'eduardo.costa@email.com', '(71) 99000-0032', '494.949.494-94', 53, 'ACTIVE'),
('Aline Silva', 'aline.silva@email.com', '(71) 99000-0033', '515.151.515-15', 54, 'ACTIVE'),
('Vinícius Fernandes', 'vinicius.fernandes@email.com', '(71) 99000-0034', '525.252.525-25', 55, 'ACTIVE'),
('Bianca Barbosa', 'bianca.barbosa@email.com', '(71) 99000-0035', '535.353.535-35', 56, 'ACTIVE'),
('Henrique Moreira', 'henrique.moreira@email.com', '(71) 99000-0036', '545.454.545-45', 57, 'ACTIVE'),
('Natália Dias', 'natalia.dias@email.com', '(71) 99000-0037', '555.555.555-56', 58, 'ACTIVE'),
('Renan Ribeiro', 'renan.ribeiro@email.com', '(71) 99000-0038', '565.656.565-65', 59, 'ACTIVE'),
('Letícia Pinto', 'leticia.pinto.paciente@email.com', '(71) 99000-0039', '575.757.575-75', 60, 'ACTIVE'),
('Márcio Azevedo', 'marcio.azevedo@email.com', '(71) 99000-0040', '585.858.585-85', 61, 'ACTIVE'),
('Daniela Monteiro', 'daniela.monteiro@email.com', '(71) 99000-0041', '595.959.595-95', 62, 'ACTIVE'),
('André Santos', 'andre.santos@email.com', '(71) 99000-0042', '616.161.616-16', 63, 'ACTIVE'),
('Cristina Costa', 'cristina.costa@email.com', '(71) 99000-0043', '626.262.626-26', 64, 'ACTIVE'),
('Paulo Silva', 'paulo.silva@email.com', '(71) 99000-0044', '636.363.636-36', 65, 'ACTIVE'),
('Sílvia Oliveira', 'silvia.oliveira@email.com', '(71) 99000-0045', '646.464.646-46', 66, 'ACTIVE'),
('Roberto Lima', 'roberto.lima.paciente@email.com', '(71) 99000-0046', '656.565.656-56', 67, 'ACTIVE'),
('Mônica Rodrigues', 'monica.rodrigues@email.com', '(71) 99000-0047', '666.666.666-67', 68, 'ACTIVE'),
('Sérgio Alves', 'sergio.alves@email.com', '(71) 99000-0048', '676.767.676-76', 69, 'ACTIVE'),
('Vera Santos', 'vera.santos@email.com', '(71) 99000-0049', '686.868.686-86', 70, 'ACTIVE'),
('Wilson Pereira', 'wilson.pereira@email.com', '(71) 99000-0050', '696.969.696-96', 71, 'ACTIVE');

\echo '   ✓ 70 pessoas inseridas (1 admin + 20 médicos + 50 pacientes)'
\echo ''

-- ========================================
-- 3. USUÁRIOS (USERS)
-- ========================================
\echo '🔐 Inserindo usuários...'

-- Senha padrão para todos: "senha123" (hash bcrypt)
-- $2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e

-- Usuários para médicos (IDs 2-21)
INSERT INTO users (person_id, password) VALUES
(2, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(3, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(4, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(5, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(6, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(7, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(8, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(9, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(10, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(11, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(12, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(13, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(14, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(15, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(16, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(17, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(18, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(19, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(20, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(21, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e');

-- Usuários para pacientes (IDs 22-71)
INSERT INTO users (person_id, password) VALUES
(22, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(23, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(24, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(25, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(26, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(27, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(28, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(29, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(30, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(31, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(32, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(33, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(34, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(35, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(36, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(37, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(38, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(39, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(40, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(41, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(42, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(43, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(44, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(45, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(46, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(47, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(48, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(49, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(50, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(51, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(52, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(53, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(54, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(55, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(56, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(57, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(58, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(59, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(60, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(61, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(62, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(63, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(64, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(65, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(66, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(67, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(68, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(69, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(70, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e'),
(71, '$2a$10$u0dwp2tWNLm7SEsL8OFK4.5JJWW3mFqQv/8stxnc3AfYXuVNPCj3e');

\echo '   ✓ 70 usuários inseridos'
\echo '   ℹ️ Senha padrão para todos: senha123'
\echo ''

-- ========================================
-- 4. ROLES DOS USUÁRIOS (USER_ROLES)
-- ========================================
\echo '🎭 Associando perfis aos usuários...'

-- Admin (user_id 1 já inserido na migration)
-- Médicos (user_ids 2-21) → ROLE_DOCTOR (role_id 2)
INSERT INTO user_roles (user_id, roles_id) VALUES
(2, 2), (3, 2), (4, 2), (5, 2), (6, 2),
(7, 2), (8, 2), (9, 2), (10, 2), (11, 2),
(12, 2), (13, 2), (14, 2), (15, 2), (16, 2),
(17, 2), (18, 2), (19, 2), (20, 2), (21, 2);

-- Pacientes (user_ids 22-71) → ROLE_PATIENT (role_id 3)
INSERT INTO user_roles (user_id, roles_id) VALUES
(22, 3), (23, 3), (24, 3), (25, 3), (26, 3),
(27, 3), (28, 3), (29, 3), (30, 3), (31, 3),
(32, 3), (33, 3), (34, 3), (35, 3), (36, 3),
(37, 3), (38, 3), (39, 3), (40, 3), (41, 3),
(42, 3), (43, 3), (44, 3), (45, 3), (46, 3),
(47, 3), (48, 3), (49, 3), (50, 3), (51, 3),
(52, 3), (53, 3), (54, 3), (55, 3), (56, 3),
(57, 3), (58, 3), (59, 3), (60, 3), (61, 3),
(62, 3), (63, 3), (64, 3), (65, 3), (66, 3),
(67, 3), (68, 3), (69, 3), (70, 3), (71, 3);

\echo '   ✓ 70 associações criadas (20 médicos + 50 pacientes)'
\echo ''

-- ========================================
-- 5. MÉDICOS (DOCTORS)
-- ========================================
\echo '👨‍⚕️ Inserindo médicos...'

INSERT INTO doctors (person_id, crm, specialty, status) VALUES
(2, 'CRM/BA 10001', 'CARDIOLOGY', 'ACTIVE'),
(3, 'CRM/BA 10002', 'DERMATOLOGY', 'ACTIVE'),
(4, 'CRM/BA 10003', 'ORTHOPEDIC', 'ACTIVE'),
(5, 'CRM/BA 10004', 'PEDIATRIC', 'ACTIVE'),
(6, 'CRM/BA 10005', 'GYNECOLOGY', 'ACTIVE'),
(7, 'CRM/BA 10006', 'PSYCHIATRY', 'ACTIVE'),
(8, 'CRM/BA 10007', 'CARDIOLOGY', 'ACTIVE'),
(9, 'CRM/BA 10008', 'NEUROLOGY', 'ACTIVE'),
(10, 'CRM/BA 10009', 'ENDOCRINOLOGY', 'ACTIVE'),
(11, 'CRM/BA 10010', 'UROLOGY', 'ACTIVE'),
(12, 'CRM/BA 10011', 'GASTROENTEROLOGY', 'ACTIVE'),
(13, 'CRM/BA 10012', 'OTORHINOLARYNGOLOGY', 'ACTIVE'),
(14, 'CRM/BA 10013', 'RHEUMATOLOGY', 'ACTIVE'),
(15, 'CRM/BA 10014', 'PNEUMOLOGY', 'ACTIVE'),
(16, 'CRM/BA 10015', 'CARDIOLOGY', 'ACTIVE'),
(17, 'CRM/BA 10016', 'OTORHINOLARYNGOLOGY', 'ACTIVE'),
(18, 'CRM/BA 10017', 'CARDIOLOGY', 'ACTIVE'),
(19, 'CRM/BA 10018', 'GERIATRIC', 'ACTIVE'),
(20, 'CRM/BA 10019', 'INFECTOLOGY', 'ACTIVE'),
(21, 'CRM/BA 10020', 'INFECTOLOGY', 'ACTIVE');

\echo '   ✓ 20 médicos inseridos'
\echo ''

-- ========================================
-- 6. PACIENTES (PATIENTS)
-- ========================================
\echo '🏥 Inserindo pacientes...'

INSERT INTO patients (person_id, status) VALUES
(22, 'ACTIVE'), (23, 'ACTIVE'), (24, 'ACTIVE'), (25, 'ACTIVE'), (26, 'ACTIVE'),
(27, 'ACTIVE'), (28, 'ACTIVE'), (29, 'ACTIVE'), (30, 'ACTIVE'), (31, 'ACTIVE'),
(32, 'ACTIVE'), (33, 'ACTIVE'), (34, 'ACTIVE'), (35, 'ACTIVE'), (36, 'ACTIVE'),
(37, 'ACTIVE'), (38, 'ACTIVE'), (39, 'ACTIVE'), (40, 'ACTIVE'), (41, 'ACTIVE'),
(42, 'ACTIVE'), (43, 'ACTIVE'), (44, 'ACTIVE'), (45, 'ACTIVE'), (46, 'ACTIVE'),
(47, 'ACTIVE'), (48, 'ACTIVE'), (49, 'ACTIVE'), (50, 'ACTIVE'), (51, 'ACTIVE'),
(52, 'ACTIVE'), (53, 'ACTIVE'), (54, 'ACTIVE'), (55, 'ACTIVE'), (56, 'ACTIVE'),
(57, 'ACTIVE'), (58, 'ACTIVE'), (59, 'ACTIVE'), (60, 'ACTIVE'), (61, 'ACTIVE'),
(62, 'ACTIVE'), (63, 'ACTIVE'), (64, 'ACTIVE'), (65, 'ACTIVE'), (66, 'ACTIVE'),
(67, 'ACTIVE'), (68, 'ACTIVE'), (69, 'ACTIVE'), (70, 'ACTIVE'), (71, 'ACTIVE');

\echo '   ✓ 50 pacientes inseridos'
\echo ''

-- ========================================
-- 7. CONSULTAS (APPOINTMENTS)
-- ========================================
\echo '📅 Inserindo consultas...'

-- Consultas passadas (últimos 30 dias) - 50 consultas
INSERT INTO appointments (date, doctor_id, patient_id, status) VALUES
-- Semana passada
(CURRENT_TIMESTAMP - INTERVAL '7 days' + INTERVAL '09:00', 1, 1, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '7 days' + INTERVAL '10:00', 2, 2, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '7 days' + INTERVAL '11:00', 3, 3, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '7 days' + INTERVAL '14:00', 4, 4, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '7 days' + INTERVAL '15:00', 5, 5, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '7 days' + INTERVAL '16:00', 6, 6, 'ATTENDED'),

(CURRENT_TIMESTAMP - INTERVAL '6 days' + INTERVAL '09:00', 7, 7, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '6 days' + INTERVAL '10:00', 8, 8, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '6 days' + INTERVAL '11:00', 9, 9, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '6 days' + INTERVAL '14:00', 10, 10, 'CANCELLED'),
(CURRENT_TIMESTAMP - INTERVAL '6 days' + INTERVAL '15:00', 11, 11, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '6 days' + INTERVAL '16:00', 12, 12, 'ATTENDED'),

(CURRENT_TIMESTAMP - INTERVAL '5 days' + INTERVAL '09:00', 13, 13, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '5 days' + INTERVAL '10:00', 14, 14, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '5 days' + INTERVAL '11:00', 15, 15, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '5 days' + INTERVAL '14:00', 16, 16, 'CANCELLED'),
(CURRENT_TIMESTAMP - INTERVAL '5 days' + INTERVAL '15:00', 17, 17, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '5 days' + INTERVAL '16:00', 18, 18, 'ATTENDED'),

(CURRENT_TIMESTAMP - INTERVAL '4 days' + INTERVAL '09:00', 19, 19, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '4 days' + INTERVAL '10:00', 20, 20, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '4 days' + INTERVAL '11:00', 1, 21, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '4 days' + INTERVAL '14:00', 2, 22, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '4 days' + INTERVAL '15:00', 3, 23, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '4 days' + INTERVAL '16:00', 4, 24, 'CANCELLED'),

(CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '09:00', 5, 25, 'SCHEDULED'),
(CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '10:00', 6, 26, 'SCHEDULED'),
(CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '11:00', 7, 27, 'SCHEDULED'),
(CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '14:00', 8, 28, 'SCHEDULED'),
(CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '15:00', 9, 29, 'SCHEDULED'),
(CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '16:00', 10, 30, 'SCHEDULED'),

-- 2 semanas atrás
(CURRENT_TIMESTAMP - INTERVAL '14 days' + INTERVAL '09:00', 11, 31, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '14 days' + INTERVAL '10:00', 12, 32, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '14 days' + INTERVAL '11:00', 13, 33, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '14 days' + INTERVAL '14:00', 14, 34, 'CANCELLED'),
(CURRENT_TIMESTAMP - INTERVAL '14 days' + INTERVAL '15:00', 15, 35, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '14 days' + INTERVAL '16:00', 16, 36, 'ATTENDED'),

(CURRENT_TIMESTAMP - INTERVAL '13 days' + INTERVAL '09:00', 17, 37, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '13 days' + INTERVAL '10:00', 18, 38, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '13 days' + INTERVAL '11:00', 19, 39, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '13 days' + INTERVAL '14:00', 20, 40, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '13 days' + INTERVAL '15:00', 1, 41, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '13 days' + INTERVAL '16:00', 2, 42, 'CANCELLED'),

-- 3 semanas atrás
(CURRENT_TIMESTAMP - INTERVAL '21 days' + INTERVAL '09:00', 3, 43, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '21 days' + INTERVAL '10:00', 4, 44, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '21 days' + INTERVAL '11:00', 5, 45, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '21 days' + INTERVAL '14:00', 6, 46, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '21 days' + INTERVAL '15:00', 7, 47, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '21 days' + INTERVAL '16:00', 8, 48, 'ATTENDED'),

(CURRENT_TIMESTAMP - INTERVAL '20 days' + INTERVAL '09:00', 9, 49, 'ATTENDED'),
(CURRENT_TIMESTAMP - INTERVAL '20 days' + INTERVAL '10:00', 10, 50, 'ATTENDED');

-- Consultas futuras (próximos 30 dias) - 60 consultas
INSERT INTO appointments (date, doctor_id, patient_id, status) VALUES
-- Amanhã
(CURRENT_TIMESTAMP + INTERVAL '1 day' + INTERVAL '09:00', 1, 1, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '1 day' + INTERVAL '10:00', 2, 2, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '1 day' + INTERVAL '11:00', 3, 3, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '1 day' + INTERVAL '14:00', 4, 4, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '1 day' + INTERVAL '15:00', 5, 5, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '1 day' + INTERVAL '16:00', 6, 6, 'SCHEDULED'),

-- Daqui a 2 dias
(CURRENT_TIMESTAMP + INTERVAL '2 days' + INTERVAL '09:00', 7, 7, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '2 days' + INTERVAL '10:00', 8, 8, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '2 days' + INTERVAL '11:00', 9, 9, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '2 days' + INTERVAL '14:00', 10, 10, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '2 days' + INTERVAL '15:00', 11, 11, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '2 days' + INTERVAL '16:00', 12, 12, 'SCHEDULED'),

-- Daqui a 3 dias
(CURRENT_TIMESTAMP + INTERVAL '3 days' + INTERVAL '09:00', 13, 13, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '3 days' + INTERVAL '10:00', 14, 14, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '3 days' + INTERVAL '11:00', 15, 15, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '3 days' + INTERVAL '14:00', 16, 16, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '3 days' + INTERVAL '15:00', 17, 17, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '3 days' + INTERVAL '16:00', 18, 18, 'SCHEDULED'),

-- Próxima semana
(CURRENT_TIMESTAMP + INTERVAL '7 days' + INTERVAL '09:00', 19, 19, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '7 days' + INTERVAL '10:00', 20, 20, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '7 days' + INTERVAL '11:00', 1, 21, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '7 days' + INTERVAL '14:00', 2, 22, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '7 days' + INTERVAL '15:00', 3, 23, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '7 days' + INTERVAL '16:00', 4, 24, 'SCHEDULED'),

(CURRENT_TIMESTAMP + INTERVAL '8 days' + INTERVAL '09:00', 5, 25, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '8 days' + INTERVAL '10:00', 6, 26, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '8 days' + INTERVAL '11:00', 7, 27, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '8 days' + INTERVAL '14:00', 8, 28, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '8 days' + INTERVAL '15:00', 9, 29, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '8 days' + INTERVAL '16:00', 10, 30, 'SCHEDULED'),

-- Daqui a 2 semanas
(CURRENT_TIMESTAMP + INTERVAL '14 days' + INTERVAL '09:00', 11, 31, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '14 days' + INTERVAL '10:00', 12, 32, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '14 days' + INTERVAL '11:00', 13, 33, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '14 days' + INTERVAL '14:00', 14, 34, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '14 days' + INTERVAL '15:00', 15, 35, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '14 days' + INTERVAL '16:00', 16, 36, 'SCHEDULED'),

(CURRENT_TIMESTAMP + INTERVAL '15 days' + INTERVAL '09:00', 17, 37, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '15 days' + INTERVAL '10:00', 18, 38, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '15 days' + INTERVAL '11:00', 19, 39, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '15 days' + INTERVAL '14:00', 20, 40, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '15 days' + INTERVAL '15:00', 1, 41, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '15 days' + INTERVAL '16:00', 2, 42, 'SCHEDULED'),

-- Daqui a 3 semanas
(CURRENT_TIMESTAMP + INTERVAL '21 days' + INTERVAL '09:00', 3, 43, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '21 days' + INTERVAL '10:00', 4, 44, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '21 days' + INTERVAL '11:00', 5, 45, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '21 days' + INTERVAL '14:00', 6, 46, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '21 days' + INTERVAL '15:00', 7, 47, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '21 days' + INTERVAL '16:00', 8, 48, 'SCHEDULED'),

(CURRENT_TIMESTAMP + INTERVAL '22 days' + INTERVAL '09:00', 9, 49, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '22 days' + INTERVAL '10:00', 10, 50, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '22 days' + INTERVAL '11:00', 11, 1, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '22 days' + INTERVAL '14:00', 12, 2, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '22 days' + INTERVAL '15:00', 13, 3, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '22 days' + INTERVAL '16:00', 14, 4, 'SCHEDULED'),

-- Daqui a 4 semanas
(CURRENT_TIMESTAMP + INTERVAL '28 days' + INTERVAL '09:00', 15, 5, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '28 days' + INTERVAL '10:00', 16, 6, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '28 days' + INTERVAL '11:00', 17, 7, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '28 days' + INTERVAL '14:00', 18, 8, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '28 days' + INTERVAL '15:00', 19, 9, 'SCHEDULED'),
(CURRENT_TIMESTAMP + INTERVAL '28 days' + INTERVAL '16:00', 20, 10, 'SCHEDULED');

\echo '   ✓ 110 consultas inseridas (50 passadas + 60 futuras)'
\echo ''

-- ========================================
-- 8. SOLICITAÇÕES DE CADASTRO (DOCTOR_REQUEST)
-- ========================================
\echo '📝 Inserindo solicitações de cadastro pendentes...'

INSERT INTO doctor_request (full_name, email, phone, cpf, crm, specialty, address_state, address_city, address_neighborhood, address_street, address_number, address_zip_code, status) VALUES
('Dr. Alexandre Borges', 'alexandre.borges@email.com', '(71) 98222-0001', '711.711.711-71', 'CRM/BA 20001', 'CARDIOLOGY', 'BA', 'Salvador', 'Pituba', 'Av. Paulo VI', '2100', '41810-001', 'PENDING'),
('Dra. Fernanda Almeida', 'fernanda.almeida@email.com', '(71) 98222-0002', '722.722.722-72', 'CRM/BA 20002', 'PEDIATRIC', 'BA', 'Salvador', 'Barra', 'Av. Oceânica', '2200', '40140-130', 'PENDING'),
('Dr. Marcelo Teixeira', 'marcelo.teixeira@email.com', '(71) 98222-0003', '733.733.733-73', 'CRM/BA 20003', 'ORTHOPEDIC', 'BA', 'Salvador', 'Ondina', 'Rua Wanderley Pinho', '2300', '40170-130', 'PENDING'),
('Dra. Claudia Mendonça', 'claudia.mendonca@email.com', '(71) 98222-0004', '744.744.744-74', 'CRM/BA 20004', 'GYNECOLOGY', 'BA', 'Salvador', 'Itaigara', 'Rua Goiás', '2400', '41825-903', 'PENDING'),
('Dr. Paulo César Rocha', 'paulo.rocha@email.com', '(71) 98222-0005', '755.755.755-75', 'CRM/BA 20005', 'DERMATOLOGY', 'BA', 'Salvador', 'Caminho das Árvores', 'Av. Tancredo Neves', '2500', '41820-021', 'PENDING');
\echo '   ✓ 5 solicitações pendentes inseridas'
\echo ''

-- ========================================
-- ESTATÍSTICAS FINAIS
-- ========================================
\echo ''
\echo '══════════════════════════════════════════'
\echo '✅ DADOS MOCKADOS INSERIDOS COM SUCESSO!'
\echo '══════════════════════════════════════════'
\echo ''
\echo '📊 Resumo dos dados inseridos:'
\echo ''
\echo '   👤 Usuários: 71 (1 admin + 20 médicos + 50 pacientes)'
\echo '   👨‍⚕️ Médicos: 20 (todas as especialidades)'
\echo '   🏥 Pacientes: 50'
\echo '   📅 Consultas: 110 (50 passadas + 60 futuras)'
\echo '   📝 Solicitações: 5 (pendentes)'
\echo ''
\echo '══════════════════════════════════════════'
\echo '🔐 CREDENCIAIS DE ACESSO'
\echo '══════════════════════════════════════════'
\echo ''
\echo '🔹 ADMIN:'
\echo '   Email: admin@medhealth.com'
\echo '   Senha: admin'
\echo ''
\echo '🔹 MÉDICOS (exemplos):'
\echo '   Email: carlos.mendes@medhealth.com'
\echo '   Email: maria.costa@medhealth.com'
\echo '   Senha: senha'
\echo ''
\echo '🔹 PACIENTES (exemplos):'
\echo '   Email: maria.silva@email.com'
\echo '   Email: jose.santos@email.com'
\echo '   Senha: senha'
\echo ''
\echo '══════════════════════════════════════════'
\echo ''
\echo '🚀 Pronto para demonstração!'
\echo ''
