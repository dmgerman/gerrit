begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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
name|client
operator|.
name|FormatUtil
operator|.
name|formatBytes
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|FormatUtil
operator|.
name|formatPercentage
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|gwt
operator|.
name|test
operator|.
name|GwtModule
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|gwt
operator|.
name|test
operator|.
name|GwtTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
annotation|@
name|GwtModule
argument_list|(
literal|"com.google.gerrit.GerritGwtUI"
argument_list|)
annotation|@
name|Ignore
DECL|class|FormatUtilTest
specifier|public
class|class
name|FormatUtilTest
extends|extends
name|GwtTest
block|{
annotation|@
name|Test
DECL|method|testFormatBytes ()
specifier|public
name|void
name|testFormatBytes
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"+/- 0 B"
argument_list|,
name|formatBytes
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"+27 B"
argument_list|,
name|formatBytes
argument_list|(
literal|27
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"+999 B"
argument_list|,
name|formatBytes
argument_list|(
literal|999
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"+1000 B"
argument_list|,
name|formatBytes
argument_list|(
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"+1023 B"
argument_list|,
name|formatBytes
argument_list|(
literal|1023
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"+1.0 KiB"
argument_list|,
name|formatBytes
argument_list|(
literal|1024
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"+1.7 KiB"
argument_list|,
name|formatBytes
argument_list|(
literal|1728
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"+108.0 KiB"
argument_list|,
name|formatBytes
argument_list|(
literal|110592
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"+6.8 MiB"
argument_list|,
name|formatBytes
argument_list|(
literal|7077888
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"+432.0 MiB"
argument_list|,
name|formatBytes
argument_list|(
literal|452984832
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"+27.0 GiB"
argument_list|,
name|formatBytes
argument_list|(
literal|28991029248L
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"+1.7 TiB"
argument_list|,
name|formatBytes
argument_list|(
literal|1855425871872L
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"+8.0 EiB"
argument_list|,
name|formatBytes
argument_list|(
literal|9223372036854775807L
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"-27 B"
argument_list|,
name|formatBytes
argument_list|(
operator|-
literal|27
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"-1.7 MiB"
argument_list|,
name|formatBytes
argument_list|(
operator|-
literal|1728
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFormatPercentage ()
specifier|public
name|void
name|testFormatPercentage
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"N/A"
argument_list|,
name|formatPercentage
argument_list|(
literal|0
argument_list|,
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"0%"
argument_list|,
name|formatPercentage
argument_list|(
literal|100
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"+25%"
argument_list|,
name|formatPercentage
argument_list|(
literal|100
argument_list|,
literal|25
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"-25%"
argument_list|,
name|formatPercentage
argument_list|(
literal|100
argument_list|,
operator|-
literal|25
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"+50%"
argument_list|,
name|formatPercentage
argument_list|(
literal|100
argument_list|,
literal|50
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"-50%"
argument_list|,
name|formatPercentage
argument_list|(
literal|100
argument_list|,
operator|-
literal|50
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"+100%"
argument_list|,
name|formatPercentage
argument_list|(
literal|100
argument_list|,
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"-100%"
argument_list|,
name|formatPercentage
argument_list|(
literal|100
argument_list|,
operator|-
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"+500%"
argument_list|,
name|formatPercentage
argument_list|(
literal|100
argument_list|,
literal|500
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

