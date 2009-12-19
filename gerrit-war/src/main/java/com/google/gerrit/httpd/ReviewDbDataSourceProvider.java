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
DECL|package|com.google.gerrit.httpd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
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
name|lifecycle
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
name|javax
operator|.
name|naming
operator|.
name|InitialContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|DataSource
import|;
end_import

begin_comment
comment|/** Provides access to the {@code ReviewDb} DataSource. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|ReviewDbDataSourceProvider
specifier|final
class|class
name|ReviewDbDataSourceProvider
implements|implements
name|Provider
argument_list|<
name|DataSource
argument_list|>
implements|,
name|LifecycleListener
block|{
DECL|field|ds
specifier|private
name|DataSource
name|ds
decl_stmt|;
annotation|@
name|Override
DECL|method|get ()
specifier|public
specifier|synchronized
name|DataSource
name|get
parameter_list|()
block|{
if|if
condition|(
name|ds
operator|==
literal|null
condition|)
block|{
name|ds
operator|=
name|open
argument_list|()
expr_stmt|;
block|}
return|return
name|ds
return|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{   }
annotation|@
name|Override
DECL|method|stop ()
specifier|public
specifier|synchronized
name|void
name|stop
parameter_list|()
block|{
if|if
condition|(
name|ds
operator|!=
literal|null
condition|)
block|{
name|closeDataSource
argument_list|(
name|ds
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|open ()
specifier|private
name|DataSource
name|open
parameter_list|()
block|{
specifier|final
name|String
name|dsName
init|=
literal|"java:comp/env/jdbc/ReviewDb"
decl_stmt|;
try|try
block|{
return|return
operator|(
name|DataSource
operator|)
operator|new
name|InitialContext
argument_list|()
operator|.
name|lookup
argument_list|(
name|dsName
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NamingException
name|namingErr
parameter_list|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"No DataSource "
operator|+
name|dsName
argument_list|,
name|namingErr
argument_list|)
throw|;
block|}
block|}
DECL|method|closeDataSource (final DataSource ds)
specifier|private
name|void
name|closeDataSource
parameter_list|(
specifier|final
name|DataSource
name|ds
parameter_list|)
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"org.apache.commons.dbcp.BasicDataSource"
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|ds
argument_list|)
condition|)
block|{
name|type
operator|.
name|getMethod
argument_list|(
literal|"close"
argument_list|)
operator|.
name|invoke
argument_list|(
name|ds
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|bad
parameter_list|)
block|{
comment|// Oh well, its not a Commons DBCP pooled connection.
block|}
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"com.mchange.v2.c3p0.DataSources"
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|ds
argument_list|)
condition|)
block|{
name|type
operator|.
name|getMethod
argument_list|(
literal|"destroy"
argument_list|,
name|DataSource
operator|.
name|class
argument_list|)
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
name|ds
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|bad
parameter_list|)
block|{
comment|// Oh well, its not a c3p0 pooled connection.
block|}
block|}
block|}
end_class

end_unit

