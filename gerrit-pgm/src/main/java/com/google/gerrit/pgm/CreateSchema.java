begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.pgm
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Stage
operator|.
name|PRODUCTION
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
name|SchemaVersion
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
name|DatabaseModule
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
name|Guice
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
name|Injector
import|;
end_import

begin_comment
comment|/**  * Creates the Gerrit 2 database schema.  */
end_comment

begin_class
DECL|class|CreateSchema
specifier|public
class|class
name|CreateSchema
extends|extends
name|AbstractProgram
block|{
annotation|@
name|Inject
DECL|field|systemConfig
specifier|private
name|SystemConfig
name|systemConfig
decl_stmt|;
annotation|@
name|Inject
DECL|field|schema
specifier|private
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
decl_stmt|;
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|int
name|run
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Injector
name|injector
init|=
name|Guice
operator|.
name|createInjector
argument_list|(
name|PRODUCTION
argument_list|,
operator|new
name|DatabaseModule
argument_list|()
argument_list|)
decl_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|this
argument_list|)
expr_stmt|;
specifier|final
name|SchemaVersion
name|sv
decl_stmt|;
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
name|sv
operator|=
name|db
operator|.
name|schemaVersion
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|SchemaVersion
operator|.
name|Key
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|sv
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Schema failed to initialize"
argument_list|)
expr_stmt|;
return|return
literal|1
return|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Gerrit Code Review initialized."
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Current settings:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  schema version =  "
operator|+
name|sv
operator|.
name|versionNbr
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  admin group    =  "
operator|+
name|systemConfig
operator|.
name|adminGroupId
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  site_path      =  "
operator|+
name|systemConfig
operator|.
name|sitePath
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit

