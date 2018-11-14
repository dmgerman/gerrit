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
name|extensions
operator|.
name|events
operator|.
name|LifecycleListener
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
name|lifecycle
operator|.
name|LifecycleModule
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
name|client
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
name|server
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
name|gwtorm
operator|.
name|server
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
name|server
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
name|Module
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

begin_comment
comment|/** Validates the current schema version. */
end_comment

begin_class
DECL|class|ReviewDbSchemaVersionCheck
specifier|public
class|class
name|ReviewDbSchemaVersionCheck
implements|implements
name|LifecycleListener
block|{
DECL|method|module ()
specifier|public
specifier|static
name|Module
name|module
parameter_list|()
block|{
return|return
operator|new
name|LifecycleModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|listener
argument_list|()
operator|.
name|to
argument_list|(
name|ReviewDbSchemaVersionCheck
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|field|schema
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
decl_stmt|;
DECL|field|site
specifier|private
specifier|final
name|SitePaths
name|site
decl_stmt|;
annotation|@
name|Inject
DECL|method|ReviewDbSchemaVersionCheck (SchemaFactory<ReviewDb> schemaFactory, SitePaths site)
specifier|public
name|ReviewDbSchemaVersionCheck
parameter_list|(
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schemaFactory
parameter_list|,
name|SitePaths
name|site
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
name|site
operator|=
name|site
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
try|try
init|(
name|ReviewDb
name|db
init|=
name|schema
operator|.
name|open
argument_list|()
init|)
block|{
specifier|final
name|CurrentSchemaVersion
name|currentVer
init|=
name|getSchemaVersion
argument_list|(
name|db
argument_list|)
decl_stmt|;
specifier|final
name|int
name|expectedVer
init|=
name|ReviewDbSchemaVersion
operator|.
name|getBinaryVersion
argument_list|()
decl_stmt|;
if|if
condition|(
name|currentVer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Schema not yet initialized."
operator|+
literal|"  Run init to initialize the schema:\n"
operator|+
literal|"$ java -jar gerrit.war init -d "
operator|+
name|site
operator|.
name|site_path
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|currentVer
operator|.
name|versionNbr
operator|<
name|expectedVer
condition|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Unsupported schema version "
operator|+
name|currentVer
operator|.
name|versionNbr
operator|+
literal|"; expected schema version "
operator|+
name|expectedVer
operator|+
literal|".  Run init to upgrade:\n"
operator|+
literal|"$ java -jar "
operator|+
name|site
operator|.
name|gerrit_war
operator|.
name|toAbsolutePath
argument_list|()
operator|+
literal|" init -d "
operator|+
name|site
operator|.
name|site_path
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|currentVer
operator|.
name|versionNbr
operator|>
name|expectedVer
condition|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Unsupported schema version "
operator|+
name|currentVer
operator|.
name|versionNbr
operator|+
literal|"; expected schema version "
operator|+
name|expectedVer
operator|+
literal|". Downgrade is not supported."
argument_list|)
throw|;
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
literal|"Cannot read schema_version"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{}
DECL|method|getSchemaVersion (ReviewDb db)
specifier|private
name|CurrentSchemaVersion
name|getSchemaVersion
parameter_list|(
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

