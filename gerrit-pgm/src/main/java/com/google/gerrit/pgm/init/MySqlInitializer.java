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

begin_class
DECL|class|MySqlInitializer
class|class
name|MySqlInitializer
implements|implements
name|DatabaseConfigInitializer
block|{
annotation|@
name|Override
DECL|method|initConfig (Section databaseSection)
specifier|public
name|void
name|initConfig
parameter_list|(
name|Section
name|databaseSection
parameter_list|)
block|{
specifier|final
name|String
name|defPort
init|=
literal|"(mysql default)"
decl_stmt|;
name|databaseSection
operator|.
name|string
argument_list|(
literal|"Server hostname"
argument_list|,
literal|"hostname"
argument_list|,
literal|"localhost"
argument_list|)
expr_stmt|;
name|databaseSection
operator|.
name|string
argument_list|(
literal|"Server port"
argument_list|,
literal|"port"
argument_list|,
name|defPort
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|databaseSection
operator|.
name|string
argument_list|(
literal|"Database name"
argument_list|,
literal|"database"
argument_list|,
literal|"reviewdb"
argument_list|)
expr_stmt|;
name|databaseSection
operator|.
name|string
argument_list|(
literal|"Database username"
argument_list|,
literal|"username"
argument_list|,
name|username
argument_list|()
argument_list|)
expr_stmt|;
name|databaseSection
operator|.
name|password
argument_list|(
literal|"username"
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

