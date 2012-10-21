begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_class
DECL|class|BaseDataSourceType
specifier|public
specifier|abstract
class|class
name|BaseDataSourceType
implements|implements
name|DataSourceType
block|{
DECL|field|driver
specifier|private
specifier|final
name|String
name|driver
decl_stmt|;
DECL|method|BaseDataSourceType (String driver)
specifier|protected
name|BaseDataSourceType
parameter_list|(
name|String
name|driver
parameter_list|)
block|{
name|this
operator|.
name|driver
operator|=
name|driver
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDriver ()
specifier|public
specifier|final
name|String
name|getDriver
parameter_list|()
block|{
return|return
name|driver
return|;
block|}
annotation|@
name|Override
DECL|method|usePool ()
specifier|public
name|boolean
name|usePool
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|getIndexScript ()
specifier|public
name|ScriptRunner
name|getIndexScript
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|getScriptRunner
argument_list|(
literal|"index_generic.sql"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getNextValScript ()
specifier|public
name|ScriptRunner
name|getNextValScript
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|ScriptRunner
operator|.
name|NOOP
return|;
block|}
DECL|method|getScriptRunner (String path)
specifier|protected
specifier|static
specifier|final
name|ScriptRunner
name|getScriptRunner
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
return|return
name|ScriptRunner
operator|.
name|NOOP
return|;
block|}
name|InputStream
name|in
init|=
name|ReviewDb
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"SQL script "
operator|+
name|path
operator|+
literal|" not found"
argument_list|)
throw|;
block|}
name|ScriptRunner
name|runner
decl_stmt|;
try|try
block|{
name|runner
operator|=
operator|new
name|ScriptRunner
argument_list|(
name|path
argument_list|,
name|in
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|runner
return|;
block|}
block|}
end_class

end_unit

