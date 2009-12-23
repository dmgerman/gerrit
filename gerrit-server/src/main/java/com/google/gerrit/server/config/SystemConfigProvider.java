begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
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
name|reviewdb
operator|.
name|CurrentSchemaVersion
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
name|reviewdb
operator|.
name|ReviewDb
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
name|reviewdb
operator|.
name|SystemConfig
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
name|schema
operator|.
name|Current
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
name|schema
operator|.
name|SchemaVersion
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|SchemaFactory
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
name|Provider
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
name|ProvisionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/** Loads the {@link SystemConfig} from the database. */
end_comment

begin_class
DECL|class|SystemConfigProvider
specifier|public
class|class
name|SystemConfigProvider
implements|implements
name|Provider
argument_list|<
name|SystemConfig
argument_list|>
block|{
DECL|field|schema
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
decl_stmt|;
annotation|@
name|Current
DECL|field|version
specifier|private
specifier|final
name|Provider
argument_list|<
name|SchemaVersion
argument_list|>
name|version
decl_stmt|;
annotation|@
name|Inject
DECL|method|SystemConfigProvider (final SchemaFactory<ReviewDb> schemaFactory, @Current final Provider<SchemaVersion> version)
specifier|public
name|SystemConfigProvider
parameter_list|(
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schemaFactory
parameter_list|,
annotation|@
name|Current
specifier|final
name|Provider
argument_list|<
name|SchemaVersion
argument_list|>
name|version
parameter_list|)
block|{
name|this
operator|.
name|schema
operator|=
name|schemaFactory
expr_stmt|;
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|SystemConfig
name|get
parameter_list|()
block|{
try|try
block|{
specifier|final
name|ReviewDb
name|db
init|=
name|schema
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|CurrentSchemaVersion
name|sVer
init|=
name|getSchemaVersion
argument_list|(
name|db
argument_list|)
decl_stmt|;
specifier|final
name|int
name|eVer
init|=
name|version
operator|.
name|get
argument_list|()
operator|.
name|getVersionNbr
argument_list|()
decl_stmt|;
if|if
condition|(
name|sVer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
literal|"Schema not yet initialized."
operator|+
literal|"  Run init to initialize the schema."
argument_list|)
throw|;
block|}
if|if
condition|(
name|sVer
operator|.
name|versionNbr
operator|!=
name|eVer
condition|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
literal|"Unsupported schema version "
operator|+
name|sVer
operator|.
name|versionNbr
operator|+
literal|"; expected schema version "
operator|+
name|eVer
operator|+
literal|".  Run init to upgrade."
argument_list|)
throw|;
block|}
specifier|final
name|List
argument_list|<
name|SystemConfig
argument_list|>
name|all
init|=
name|db
operator|.
name|systemConfig
argument_list|()
operator|.
name|all
argument_list|()
operator|.
name|toList
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|all
operator|.
name|size
argument_list|()
condition|)
block|{
case|case
literal|1
case|:
return|return
name|all
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
case|case
literal|0
case|:
throw|throw
operator|new
name|OrmException
argument_list|(
literal|"system_config table is empty"
argument_list|)
throw|;
default|default:
throw|throw
operator|new
name|OrmException
argument_list|(
literal|"system_config must have exactly 1 row;"
operator|+
literal|" found "
operator|+
name|all
operator|.
name|size
argument_list|()
operator|+
literal|" rows instead"
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Cannot read system_config"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getSchemaVersion (final ReviewDb db)
specifier|private
name|CurrentSchemaVersion
name|getSchemaVersion
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|)
block|{
try|try
block|{
return|return
name|db
operator|.
name|schemaVersion
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|CurrentSchemaVersion
operator|.
name|Key
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

