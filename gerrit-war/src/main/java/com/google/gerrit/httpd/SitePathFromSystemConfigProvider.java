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
name|config
operator|.
name|SitePath
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
name|java
operator|.
name|io
operator|.
name|File
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
comment|/** Provides {@link java.io.File} annotated with {@link SitePath}. */
end_comment

begin_class
DECL|class|SitePathFromSystemConfigProvider
class|class
name|SitePathFromSystemConfigProvider
implements|implements
name|Provider
argument_list|<
name|File
argument_list|>
block|{
DECL|field|path
specifier|private
specifier|final
name|File
name|path
decl_stmt|;
annotation|@
name|Inject
DECL|method|SitePathFromSystemConfigProvider (SchemaFactory<ReviewDb> schemaFactory)
name|SitePathFromSystemConfigProvider
parameter_list|(
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schemaFactory
parameter_list|)
throws|throws
name|OrmException
block|{
name|path
operator|=
name|read
argument_list|(
name|schemaFactory
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|File
name|get
parameter_list|()
block|{
return|return
name|path
return|;
block|}
DECL|method|read (SchemaFactory<ReviewDb> schemaFactory)
specifier|private
specifier|static
name|File
name|read
parameter_list|(
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schemaFactory
parameter_list|)
throws|throws
name|OrmException
block|{
name|ReviewDb
name|db
init|=
name|schemaFactory
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
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
operator|new
name|File
argument_list|(
name|all
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|sitePath
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
block|}
end_class

end_unit

