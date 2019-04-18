begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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

begin_class
DECL|class|NoteDbSchemaVersionCheck
specifier|public
class|class
name|NoteDbSchemaVersionCheck
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
name|NoteDbSchemaVersionCheck
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|field|versionManager
specifier|private
specifier|final
name|NoteDbSchemaVersionManager
name|versionManager
decl_stmt|;
DECL|field|sitePaths
specifier|private
specifier|final
name|SitePaths
name|sitePaths
decl_stmt|;
annotation|@
name|Inject
DECL|method|NoteDbSchemaVersionCheck (NoteDbSchemaVersionManager versionManager, SitePaths sitePaths)
name|NoteDbSchemaVersionCheck
parameter_list|(
name|NoteDbSchemaVersionManager
name|versionManager
parameter_list|,
name|SitePaths
name|sitePaths
parameter_list|)
block|{
name|this
operator|.
name|versionManager
operator|=
name|versionManager
expr_stmt|;
name|this
operator|.
name|sitePaths
operator|=
name|sitePaths
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
block|{
name|int
name|current
init|=
name|versionManager
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|current
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Schema not yet initialized. Run init to initialize the schema:\n"
operator|+
literal|"$ java -jar gerrit.war init -d %s"
argument_list|,
name|sitePaths
operator|.
name|site_path
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|int
name|expected
init|=
name|NoteDbSchemaVersions
operator|.
name|LATEST
decl_stmt|;
if|if
condition|(
name|current
operator|!=
name|expected
condition|)
block|{
name|String
name|advice
init|=
name|current
operator|>
name|expected
condition|?
literal|"Downgrade is not supported"
else|:
name|String
operator|.
name|format
argument_list|(
literal|"Run init to upgrade:\n$ java -jar %s init -d %s"
argument_list|,
name|sitePaths
operator|.
name|gerrit_war
operator|.
name|toAbsolutePath
argument_list|()
argument_list|,
name|sitePaths
operator|.
name|site_path
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ProvisionException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Unsupported schema version %d; expected schema version %d. %s"
argument_list|,
name|current
argument_list|,
name|expected
argument_list|,
name|advice
argument_list|)
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|StorageException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Failed to read NoteDb schema version"
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
block|{
comment|// Do nothing.
block|}
block|}
end_class

end_unit

