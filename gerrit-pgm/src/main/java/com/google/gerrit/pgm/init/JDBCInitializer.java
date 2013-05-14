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
DECL|package|com.google.gerrit.pgm.init
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|init
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|init
operator|.
name|InitUtil
operator|.
name|username
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Strings
import|;
end_import

begin_class
DECL|class|JDBCInitializer
class|class
name|JDBCInitializer
implements|implements
name|DatabaseConfigInitializer
block|{
annotation|@
name|Override
DECL|method|initConfig (Section database)
specifier|public
name|void
name|initConfig
parameter_list|(
name|Section
name|database
parameter_list|)
block|{
name|boolean
name|hasUrl
init|=
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|database
operator|.
name|get
argument_list|(
literal|"url"
argument_list|)
argument_list|)
operator|!=
literal|null
decl_stmt|;
name|database
operator|.
name|string
argument_list|(
literal|"URL"
argument_list|,
literal|"url"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|guessDriver
argument_list|(
name|database
argument_list|)
expr_stmt|;
name|database
operator|.
name|string
argument_list|(
literal|"Driver class name"
argument_list|,
literal|"driver"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|database
operator|.
name|string
argument_list|(
literal|"Database username"
argument_list|,
literal|"username"
argument_list|,
name|hasUrl
condition|?
literal|null
else|:
name|username
argument_list|()
argument_list|)
expr_stmt|;
name|database
operator|.
name|password
argument_list|(
literal|"username"
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
block|}
DECL|method|guessDriver (Section database)
specifier|private
name|void
name|guessDriver
parameter_list|(
name|Section
name|database
parameter_list|)
block|{
name|String
name|url
init|=
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|database
operator|.
name|get
argument_list|(
literal|"url"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
operator|&&
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|database
operator|.
name|get
argument_list|(
literal|"driver"
argument_list|)
argument_list|)
condition|)
block|{
if|if
condition|(
name|url
operator|.
name|startsWith
argument_list|(
literal|"jdbc:h2:"
argument_list|)
condition|)
block|{
name|database
operator|.
name|set
argument_list|(
literal|"driver"
argument_list|,
literal|"org.h2.Driver"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|url
operator|.
name|startsWith
argument_list|(
literal|"jdbc:mysql:"
argument_list|)
condition|)
block|{
name|database
operator|.
name|set
argument_list|(
literal|"driver"
argument_list|,
literal|"com.mysql.jdbc.Driver"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|url
operator|.
name|startsWith
argument_list|(
literal|"jdbc:postgresql:"
argument_list|)
condition|)
block|{
name|database
operator|.
name|set
argument_list|(
literal|"driver"
argument_list|,
literal|"org.postgresql.Driver"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

