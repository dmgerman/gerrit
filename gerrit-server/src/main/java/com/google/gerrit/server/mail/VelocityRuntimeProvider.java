begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.mail
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|mail
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
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|runtime
operator|.
name|RuntimeConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|runtime
operator|.
name|RuntimeInstance
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_comment
comment|/** Configures Velocity template engine for sending email. */
end_comment

begin_class
DECL|class|VelocityRuntimeProvider
specifier|public
class|class
name|VelocityRuntimeProvider
implements|implements
name|Provider
argument_list|<
name|RuntimeInstance
argument_list|>
block|{
DECL|field|site
specifier|private
specifier|final
name|SitePaths
name|site
decl_stmt|;
annotation|@
name|Inject
DECL|method|VelocityRuntimeProvider (SitePaths site)
name|VelocityRuntimeProvider
parameter_list|(
name|SitePaths
name|site
parameter_list|)
block|{
name|this
operator|.
name|site
operator|=
name|site
expr_stmt|;
block|}
DECL|method|get ()
specifier|public
name|RuntimeInstance
name|get
parameter_list|()
block|{
name|String
name|rl
init|=
literal|"resource.loader"
decl_stmt|;
name|String
name|pkg
init|=
literal|"org.apache.velocity.runtime.resource.loader"
decl_stmt|;
name|Properties
name|p
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|p
operator|.
name|setProperty
argument_list|(
name|RuntimeConstants
operator|.
name|RUNTIME_LOG_LOGSYSTEM_CLASS
argument_list|,
literal|"org.apache.velocity.runtime.log.SimpleLog4JLogSystem"
argument_list|)
expr_stmt|;
name|p
operator|.
name|setProperty
argument_list|(
literal|"runtime.log.logsystem.log4j.category"
argument_list|,
literal|"velocity"
argument_list|)
expr_stmt|;
if|if
condition|(
name|site
operator|.
name|mail_dir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|p
operator|.
name|setProperty
argument_list|(
name|rl
argument_list|,
literal|"file, class"
argument_list|)
expr_stmt|;
name|p
operator|.
name|setProperty
argument_list|(
literal|"file."
operator|+
name|rl
operator|+
literal|".class"
argument_list|,
name|pkg
operator|+
literal|".FileResourceLoader"
argument_list|)
expr_stmt|;
name|p
operator|.
name|setProperty
argument_list|(
literal|"file."
operator|+
name|rl
operator|+
literal|".path"
argument_list|,
name|site
operator|.
name|mail_dir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|setProperty
argument_list|(
literal|"class."
operator|+
name|rl
operator|+
literal|".class"
argument_list|,
name|pkg
operator|+
literal|".ClasspathResourceLoader"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|p
operator|.
name|setProperty
argument_list|(
name|rl
argument_list|,
literal|"class"
argument_list|)
expr_stmt|;
name|p
operator|.
name|setProperty
argument_list|(
literal|"class."
operator|+
name|rl
operator|+
literal|".class"
argument_list|,
name|pkg
operator|+
literal|".ClasspathResourceLoader"
argument_list|)
expr_stmt|;
block|}
name|RuntimeInstance
name|ri
init|=
operator|new
name|RuntimeInstance
argument_list|()
decl_stmt|;
try|try
block|{
name|ri
operator|.
name|init
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Cannot configure Velocity templates"
argument_list|,
name|err
argument_list|)
throw|;
block|}
return|return
name|ri
return|;
block|}
block|}
end_class

end_unit

