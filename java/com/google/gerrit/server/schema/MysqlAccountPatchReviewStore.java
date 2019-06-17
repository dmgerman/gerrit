begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|exceptions
operator|.
name|DuplicateKeyException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|exceptions
operator|.
name|StorageException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|GerritServerConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|SitePaths
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|ThreadSettingsConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Statement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|MysqlAccountPatchReviewStore
specifier|public
class|class
name|MysqlAccountPatchReviewStore
extends|extends
name|JdbcAccountPatchReviewStore
block|{
annotation|@
name|Inject
DECL|method|MysqlAccountPatchReviewStore ( @erritServerConfig Config cfg, SitePaths sitePaths, ThreadSettingsConfig threadSettingsConfig)
name|MysqlAccountPatchReviewStore
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
name|SitePaths
name|sitePaths
parameter_list|,
name|ThreadSettingsConfig
name|threadSettingsConfig
parameter_list|)
block|{
name|super
argument_list|(
name|cfg
argument_list|,
name|sitePaths
argument_list|,
name|threadSettingsConfig
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|convertError (String op, SQLException err)
specifier|public
name|StorageException
name|convertError
parameter_list|(
name|String
name|op
parameter_list|,
name|SQLException
name|err
parameter_list|)
block|{
switch|switch
condition|(
name|err
operator|.
name|getErrorCode
argument_list|()
condition|)
block|{
case|case
literal|1022
case|:
comment|// ER_DUP_KEY
case|case
literal|1062
case|:
comment|// ER_DUP_ENTRY
case|case
literal|1169
case|:
comment|// ER_DUP_UNIQUE;
return|return
operator|new
name|DuplicateKeyException
argument_list|(
literal|"ACCOUNT_PATCH_REVIEWS"
argument_list|,
name|err
argument_list|)
return|;
default|default:
if|if
condition|(
name|err
operator|.
name|getCause
argument_list|()
operator|==
literal|null
operator|&&
name|err
operator|.
name|getNextException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|err
operator|.
name|initCause
argument_list|(
name|err
operator|.
name|getNextException
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|StorageException
argument_list|(
name|op
operator|+
literal|" failure on ACCOUNT_PATCH_REVIEWS"
argument_list|,
name|err
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|doCreateTable (Statement stmt)
specifier|protected
name|void
name|doCreateTable
parameter_list|(
name|Statement
name|stmt
parameter_list|)
throws|throws
name|SQLException
block|{
name|stmt
operator|.
name|executeUpdate
argument_list|(
literal|"CREATE TABLE IF NOT EXISTS account_patch_reviews ("
operator|+
literal|"account_id INTEGER DEFAULT 0 NOT NULL, "
operator|+
literal|"change_id INTEGER DEFAULT 0 NOT NULL, "
operator|+
literal|"patch_set_id INTEGER DEFAULT 0 NOT NULL, "
operator|+
literal|"file_name VARCHAR(255) DEFAULT '' NOT NULL, "
operator|+
literal|"CONSTRAINT primary_key_account_patch_reviews "
operator|+
literal|"PRIMARY KEY (change_id, patch_set_id, account_id, file_name)"
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

